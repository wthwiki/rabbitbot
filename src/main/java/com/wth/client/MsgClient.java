package com.wth.client;

import com.alibaba.fastjson.JSONObject;
import com.wth.constant.GocqActionEnum;
import com.wth.constant.MessageEventEnum;
import com.wth.constant.event.MetaEventEnum;
import com.wth.constant.event.PostTypeEnum;
import com.wth.entity.chart.ChatGTP;
import com.wth.entity.get.Message;
import com.wth.entity.response.ForwardMsg;
import com.wth.entity.response.Params;
import com.wth.entity.response.Request;
import com.wth.factory.ThreadPoolFactory;
import com.wth.messagehandlers.MessageHandleRegister;
import com.wth.thread.MsgEventTask;
import com.wth.thread.ReconnectTask;
import lombok.extern.slf4j.Slf4j;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.util.List;

@Slf4j
@ClientEndpoint
public class MsgClient {

    private Session session;
    private static MsgClient INSTANCE;
    private volatile static boolean connecting =false;

    private MsgClient(String url) throws DeploymentException, IOException {
        // 获取与go-cq服务端会话
        session= ContainerProvider.getWebSocketContainer().connectToServer(this, URI.create(url));
        //
        Thread thread = new Thread(new ChatGTP());
        thread.start();
    }

    public synchronized static boolean connect(String url){
        try {
            // 创建实例
            INSTANCE=new MsgClient(url);
            connecting=false;
            return true;
        } catch (Exception e) {
            System.out.println("连接失败");
            e.printStackTrace();
            return false;
        }
    }
    public synchronized static void reConnect(){
        if(!connecting){
            connecting=true;
            if(INSTANCE!=null){
                INSTANCE.session=null;
                INSTANCE=null;
            }
            ReconnectTask.execute();
        }
    }

    public static void sendMsg(GocqActionEnum action,String id, List<ForwardMsg> params){
        Request<Params> collectionAnswerBox = new Request<>();

        collectionAnswerBox.setAction(action.getAction());

        Params params1 = new Params();
        params1.setGroup_id(id);
        params1.setUser_id(id);
        params1.setMessages(params);
        collectionAnswerBox.setParams(params1);
        sendMsg(collectionAnswerBox);
    }

    @OnOpen
    public void onOpen(Session session){
        System.out.println("连接成功");
    }

//    第一次调用这个方法的注解
    //    @Override
//    public boolean onMessage(Message message) {
//        Request<Params> request = new Request<>();
//        request.setAction("send_msg");
//        Params params = new Params();
//        params.setUser_id(message.getUser_id()); // 这里获取对象的qq号
//        params.setGroup_id(message.getGroup_id());
//        params.setMessage(message.getMessage()); //设置返回的消息，这里复读机，就返回复读消息
//        params.setMessage_type(message.getMessage_type());// 设置回复参数类型，与传入的参数类型相同
//        params.setAuto_escape(true); // 设置是否解析为cq
//        request.setParams(params);
//        MsgClient.sendMsg(JSONObject.toJSONString(request));
//        return false;
//    }

    @OnMessage
    public void onMessage(String json){
        final Message message = JSONObject.parseObject(json, Message.class);
        log.info(String.valueOf(message));
        if(PostTypeEnum.meta_event.toString().equals(message.getPostType()) && MetaEventEnum.heartbeat.toString().equals(message.getMetaEventType())){
            // 心跳包
            return;
        }
        ThreadPoolFactory.getEventThreadPool().execute(new MsgEventTask(message));
//        if("message".equals(message.getPost_type())){
//            MessageHandleRegister.handle(message, message.getMessage());
//        }
    }
    @OnClose
    public void onClose(Session session){
        System.out.println("连接关闭");
        reConnect();
    }
    @OnError
    public void onError(Session session ,Throwable throwable){
       log.info("throwable:"+throwable);
        reConnect();
    }

    /**
     * @param target to
     * @param groupId 群号
     * @param type 群聊 or 私聊
     * @param message 消息
     * @param action 动作类型 详见gocq文档 https://docs.go-cqhttp.org/api
     * @param autoEscape 是否不解析cq码 true:不解析 false:解析
     */
    public static void sendMessage(String target, String groupId , MessageEventEnum type, String message, GocqActionEnum action, boolean autoEscape){

        Request<Params> box = new Request<>();
        Params answer = new Params();
        answer.setMessage(message);
        answer.setMessage_type(type.getType());
        answer.setUser_id(target);
        answer.setAuto_escape(autoEscape);
        answer.setGroup_id(groupId);
        box.setParams(answer);
        box.setAction(action.getAction());
        sendMessage(box);
    }

    public static <T> void sendMessage(T box){
        String boxJson = null;
        try {
            boxJson = JSONObject.toJSONString(box);
            INSTANCE.session.getAsyncRemote().sendText(boxJson);
            log.info("bot发送了：{}",boxJson);
        } catch (Exception e){
            log.error("发送消息时异常,消息:{}",boxJson,e);
        }
    }


    public static void sendMsg(String json){
        MsgClient.INSTANCE.session.getAsyncRemote().sendText(json);
    }

    /**
     * 发送消息接口
     * @param target  个人号
     * @param groupId 群号
     * @param type
     * @param message 发送信息
     * @param action
     * @param autoEscape 是否自动
     */
    public static void sendMsg(String target, String groupId , String type, String message, GocqActionEnum action, boolean autoEscape){

        Request<Params> box = new Request<>();

        Params params = new Params();
        params.setMessage(message);
        params.setMessage_type(type);
        params.setUser_id(target);
        params.setGroup_id(groupId);
        params.setAuto_escape(autoEscape);

        box.setParams(params);
        box.setAction(action.getAction());
        sendMsg(box);
    }

    /**
     * 发送消息接口
     * @param box 表示需要发送的消息
     * @param <T> 同一接口。
     */
    public static <T> void sendMsg(T box){
        String boxJson = null;
        try {
            boxJson = JSONObject.toJSONString(box);
            INSTANCE.session.getAsyncRemote().sendText(boxJson);
            log.info("bot发送了：{}",boxJson);
        } catch (Exception e){
            log.error("发送消息时异常,消息:{}",boxJson,e);
        }
    }

    /**
     * 判断是否连接go-cq服务器
     * @return 返回是否连接
     */
    public static boolean connected(){
        return INSTANCE != null && INSTANCE.session != null && INSTANCE.session.isOpen();
    }

}
