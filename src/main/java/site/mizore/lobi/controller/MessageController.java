package site.mizore.lobi.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import site.mizore.lobi.api.CommonResult;
import site.mizore.lobi.common.CommonPage;
import site.mizore.lobi.entity.po.Message;
import site.mizore.lobi.entity.vo.MessageVO;
import site.mizore.lobi.enums.MessageTypeEnum;
import site.mizore.lobi.service.MessageService;

@Api("MessageController")
@RestController
@RequestMapping("/message")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @ApiOperation("获取消息")
    @GetMapping("/page/my")
    public CommonResult getMyMessagePage(@RequestParam("type") MessageTypeEnum type,
                                         @RequestParam("page") Integer page,
                                         @RequestParam("size") Integer size) {
        CommonPage<MessageVO> messagePage = messageService.getMyMessagePage(type, page, size);
        return CommonResult.success(messagePage);
    }

    @ApiOperation("改变消息状态为已读")
    @GetMapping("/read/{id}")
    public CommonResult setMessageReaded(@PathVariable("id") Long id) {
        Message message = messageService.setMessageReaded(id);
        return CommonResult.success(message);
    }

    @ApiOperation("删除消息")
    @DeleteMapping("/delete/{id}")
    public CommonResult deleteMessage(@PathVariable("id") Long id) {
        boolean result = messageService.deleteMessage(id);
        return CommonResult.success(result);
    }

    @ApiOperation("已读某类型全部消息")
    @GetMapping("/read")
    public CommonResult setAllMessageReaded(@RequestParam("type") MessageTypeEnum type) {
        messageService.setAllMessageReaded(type);
        return CommonResult.success(true);
    }

    @ApiOperation("删除某类型全部消息")
    @DeleteMapping("/delete")
    public CommonResult deleteAllMessage(@RequestParam("type") MessageTypeEnum type) {
        messageService.deleteAllMessage(type);
        return CommonResult.success(true);
    }

    @ApiOperation("是否有未读消息")
    @GetMapping("/unread")
    public CommonResult hasUnreadMessage() {
        boolean result = messageService.hasUnreadMessage();
        return CommonResult.success(result);
    }

}
