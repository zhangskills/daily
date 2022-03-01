package my.service;

import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.cp.api.WxCpExternalContactService;
import me.chanjar.weixin.cp.api.impl.WxCpServiceImpl;
import me.chanjar.weixin.cp.bean.external.WxCpUserExternalGroupChatList;
import me.chanjar.weixin.cp.bean.message.WxCpMessage;
import me.chanjar.weixin.cp.config.impl.WxCpDefaultConfigImpl;

@Slf4j
public class MyWxCpService {

    private String corpId;
    private String corpSecret;
    private Integer agentId;

    public MyWxCpService(String corpId, String corpSecret, Integer agentId) {
        this.corpId = corpId;
        this.corpSecret = corpSecret;
        this.agentId = agentId;
    }

    public void sendByDaily(String msg) throws WxErrorException {
        WxCpDefaultConfigImpl config = new WxCpDefaultConfigImpl();
        config.setCorpId(corpId); // 设置微信企业号的appid
        config.setCorpSecret(corpSecret); // 设置微信企业号的app corpSecret
        config.setAgentId(agentId); // 设置微信企业号应用ID

        WxCpServiceImpl wxCpService = new WxCpServiceImpl();
        wxCpService.setWxCpConfigStorage(config);
        WxCpExternalContactService wxCpExternalContactService = wxCpService.getExternalContactService();
        WxCpUserExternalGroupChatList list = wxCpExternalContactService.listGroupChat(1, null, 0, null);
        list.getGroupChatList().forEach(m -> {
            log.info("groupId:{}", m.getChatId());
        });

        WxCpMessage message = WxCpMessage.TEXT().content(msg).toUser("@all").build();
        // WxCpMessage message =
        // WxCpMessage.TEXT().toUser("zhangmengjie").content(content).build();
        log.info("send message");

        wxCpService.getMessageService().send(message);
    }

    public static void main(String[] args) throws WxErrorException {
    }
}
