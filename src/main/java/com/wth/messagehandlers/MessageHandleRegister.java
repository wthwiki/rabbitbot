package com.wth.messagehandlers;

import com.wth.constant.MessageEventEnum;
import com.wth.entity.get.Message;
import com.wth.messagehandlers.message.IMessageEvent;
import com.wth.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

@Slf4j
@Component
public class MessageHandleRegister {
    // 保存注入的功能
    private static Map<String, IMessageEvent> messageEventMap;

    private static MessageService messageService;

    public static Map<String, IMessageEvent> getMessageEventMap() {
        return messageEventMap;
    }

    @Autowired
    public void setMessageEventMap(Map<String,IMessageEvent> map,MessageService messageService){
        MessageHandleRegister.messageEventMap =map;
        MessageHandleRegister.messageService=messageService;
    }

    // 更具功能权重进行了排序。
    private static List<IMessageEvent> container = new CopyOnWriteArrayList<>();


    /**
     *  PostConstruct 会在属性注入后执行这个方法。
     */
    @PostConstruct
    public void init(){
        container =new ArrayList<>(messageEventMap.size());
        for(IMessageEvent value: messageEventMap.values()){
            container.add(value);
        }
        //
        checkWeight();
        sortByWeight();
    }

    private void checkWeight(){
        List<Integer> collect = container.stream().map(IMessageEvent::weight).collect(Collectors.toList());
        HashSet<Integer> set = new HashSet<>(collect);
        if(set.size()!=collect.size()){
            throw new RuntimeException("Duplicate weight appear");
        }
    }


    /**
     * 按照功能权重进行排序
     * @return 返回功能数量
     */
    private static  int sortByWeight(){
        container = container.stream().sorted(Comparator.comparing(IMessageEvent::weight).reversed()).collect(Collectors.toList());
        return container.size();
    }


    /**
     * 群禁用功能，map保存
     * key 群号
     * value 禁用功能集合
     * element 类名(全路径)
     */
    private static Map<String,List<String>> groupBanFunctionMap =new ConcurrentHashMap<>(0);

    public static <T extends IMessageEventType> void setGroupBanFunction(String groupId,Class<T> clazz){
        setGroupBanFunction(groupId,clazz.getName());
    }

    private static void setGroupBanFunction(String groupId,String className){
        if(groupBanFunctionMap.containsKey(groupId)){
            List<String> classNames = groupBanFunctionMap.get(groupId);
            classNames.add(className);
        }else{
            List<String> classNames = new ArrayList<>(1);
            classNames.add(className);
            groupBanFunctionMap.put(groupId,classNames);
        }
    }

    public static <T extends IMessageEvent> void groupUnbanFunction(String groupId,Class<T> tClass){
        groupUnbanFunction(groupId,tClass.getName());
    }

    private static void groupUnbanFunction(String groupId,String className){
        if(groupBanFunctionMap.containsKey(groupId)){
            List<String> classNames = groupBanFunctionMap.get(groupId);
            if(!CollectionUtils.isEmpty(classNames)){
                classNames.remove(className);
            }
        }
    }

    private static boolean isBanFunctionByGroup(IMessageEvent iMessageEvent, String group_id) {
        return isBanFunctionByGroup(iMessageEvent.getClass(),group_id);
    }

    /**
     * 公开方法
     * @param tClass 对外提供的isBanFunctionByGroup方法,不能直接传IMessageEventType对象 必须为Class对象
     * @param groupId
     * @param <T>
     * @return
     */
    public static <T extends IMessageEventType> boolean isBanFunctionByGroup(Class<T> tClass,String groupId){
        if(groupBanFunctionMap.size() == 0|| !groupBanFunctionMap.containsKey(groupId)){
            return false;
        }
        List<String> classPaths = groupBanFunctionMap.get(groupId);
        if(classPaths!=null&&CollectionUtils.isEmpty(classPaths)){
            return false;
        }
        return classPaths.contains(tClass.getName());
    }


    /**
     * 查找处理类
     * @param fun 可以是name也可以是id(weight)
     * @return
     */
    public static IMessageEvent findHandler(String fun){
        Integer funId;
        IMessageEvent messageEventType;
        try {
            funId = Integer.valueOf(fun);
            messageEventType = findHandlerByWeight(funId);
        }catch (Exception e){
            messageEventType = findHandlerByName(fun);
        }
        return messageEventType;
    }

    private static IMessageEvent findHandlerByName(String funName){
        for (Map.Entry<String, IMessageEvent> eventTypeEntry : messageEventMap.entrySet()) {
            if(eventTypeEntry.getValue().name().equals(funName)){
                return eventTypeEntry.getValue();
            }
        }
        return null;
    }

    private static IMessageEvent findHandlerByWeight(int weight){
        for (Map.Entry<String, IMessageEvent> eventTypeEntry : messageEventMap.entrySet()) {
            if(eventTypeEntry.getValue().weight() == weight){
                return eventTypeEntry.getValue();
            }
        }
        return null;
    }

    public static void handle(Message message,final String command){
        messageService.insertMessage(message);
        log.info("存入消息");
        String message_type = message.getMessageType();
        // 群消息
        if(MessageEventEnum.group.getType().equals(message_type)){
            log.info("群聊");
            for (IMessageEvent iMessageEvent: container) {
                if(isBanFunctionByGroup(iMessageEvent,message.getGroupId())){
                    continue;
                }
                iMessageEvent.onMessage(message,message.getMessage());
            }
            //私聊消息
        }else if(MessageEventEnum.privat.getType().equals(message_type)){
            log.info("私聊");
            for(IMessageEvent iMessageEvent: container){
                if(iMessageEvent.onMessage(message,command)){
                    break;
                }
            }
        }else{

        }
    }

//    public static <T extends IMessageEvent> boolean exist(Class<T> tClass){
//        T bean = ApplicationContext.getBean();
//        return container.contains(bean);
//    }
}
