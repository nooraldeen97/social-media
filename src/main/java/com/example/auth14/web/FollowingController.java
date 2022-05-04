package com.example.auth14.web;

import org.apache.tomcat.jni.Error;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Controller
public class FollowingController {

    @Autowired
    ChatUserRepository chatUserRepository;



    @GetMapping("/users")
    String getUsers(Model model,HttpServletRequest request){
        List<ChatUser> users=chatUserRepository.findAll();
        model.addAttribute("currentUser",request.getSession().getAttribute("username"));
        model.addAttribute("users",users);
        model.addAttribute("currentUserObject",chatUserRepository.findByUsername((String) request.getSession().getAttribute("username")));
        model.addAttribute("id",chatUserRepository.findByUsername((String) request.getSession().getAttribute("username")).id);
//        model.addAttribute("imgURL",chatUserRepository.findByUsername(request.getSession().getAttribute("username").toString()).getPhotosImagePath());

        return "users";
    }

    @PostMapping("/follow/{id}")
    RedirectView addFollowers(@PathVariable Long id, RedirectAttributes re, HttpServletRequest request ,Model model){
        ChatUser chatUser=chatUserRepository.getById(id);
       Set<ChatUser> followers=chatUser.getFollowers();
       followers.add(chatUserRepository.findByUsername(request.getSession().getAttribute("username").toString()));
       chatUserRepository.save(chatUser);
        return new RedirectView("/users");
    }

    @PostMapping("/unfollow/{id}")
    RedirectView unfollow(@PathVariable Long id ,HttpServletRequest request,Model model){
        ChatUser chatUser=chatUserRepository.getById(id);
        Set <ChatUser> followers=chatUser.getFollowers();
        followers.remove(chatUserRepository.findByUsername(request.getSession().getAttribute("username").toString()));
        chatUserRepository.save(chatUser);
//        model.addAttribute("cond",true);
        return new RedirectView("/users");
    }
}
