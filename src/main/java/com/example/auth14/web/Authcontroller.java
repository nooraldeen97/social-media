package com.example.auth14.web;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

@Controller
public class Authcontroller {

    @Autowired
    ChatUserRepository chatUserRepository;

    @Autowired
    PosterRepository posterRepository;

    @GetMapping("/")
    String rootData(HttpServletRequest request, Model model){
        HttpSession session=request.getSession();
        if(session.getAttribute("username")!=null)
        {
            model.addAttribute("userData",session.getAttribute("username"));

            List<Poster> posters=posterRepository.findAllByChatUserUsername((String) session.getAttribute("username"));
            System.out.println(posters);
            return "index";
        }else{
            return "redirect:/login";
        }

    }

    @GetMapping("/login")
    String getLogin(Model model){
        return "login";
    }

    @PostMapping("/login")

    RedirectView login(HttpServletRequest request, String username, String password, RedirectAttributes re){
        ChatUser userFromDB=chatUserRepository.findByUsername(username);

        if(userFromDB==null || !BCrypt.checkpw(password,userFromDB.password)){
            re.addFlashAttribute("notLogged",true);
            return  new RedirectView("/login");
        }

        //adding the username to the session so i can get the data from the session and display it .
        HttpSession session=request.getSession();
        session.setAttribute("username",username);

        ///////try
//        posterRepository.save(new Poster("nice",true,userFromDB));
    return new RedirectView("/user/"+userFromDB.id);
    }

    @GetMapping("/signup")
    String getSignUp(){
        return "signup";
    }

    @PostMapping("/signup")
    RedirectView signUserUp(String username,String password){

        ChatUser userFromDB=chatUserRepository.findByUsername(username);

        if(userFromDB==null || !BCrypt.checkpw(password,userFromDB.password)){
            String hashedPassword=BCrypt.hashpw(password,BCrypt.gensalt(12));
            ChatUser chatUser=new ChatUser(username,hashedPassword);

            chatUserRepository.save(chatUser);
        }


        return new RedirectView("/login");
    }

    @PostMapping("/logout")
    RedirectView logOut(HttpServletRequest request){
        HttpSession session=request.getSession();
        session.invalidate();

        return new RedirectView("/login");
    }


    @GetMapping("/user/{id}")
    String getMyPost(@PathVariable Long id ,Model m){
       List<Poster> list = posterRepository.findAllByChatUserId(id);
       m.addAttribute("posts",list);
       m.addAttribute("id",id);
        return "myPosts";
    }

    @PostMapping("/addPost/{id}")
    RedirectView addPost(@PathVariable Long id,String content,boolean isPublic){

        posterRepository.save(new Poster(content,isPublic,chatUserRepository.getById(id)));
        return new RedirectView("/user/"+id);
    }

    @GetMapping("/publicPost")
    String getPublicPosts(Model model,HttpServletRequest request){

        HashMap<Poster,String> hashMap=new HashMap<>();
       List<Poster>posters= posterRepository.findAllByIsPublic(true);

        for (Poster p:posters) {
            hashMap.put(p,chatUserRepository.findById(p.chatUser.getId()).get().username);
        }
        model.addAttribute("current",request.getSession().getAttribute("username"));
        model.addAttribute("hashMapList",hashMap);
        model.addAttribute("id",chatUserRepository.findByUsername((String) request.getSession().getAttribute("username")).id);
        return "public";
    }


    // delete post
    @DeleteMapping("/delete/{idp}")
    RedirectView deletePost(@PathVariable int idp , HttpServletRequest request){
        posterRepository.deleteById(idp);
        HttpSession session=request.getSession();

        return new RedirectView("/user/"+chatUserRepository.findByUsername((String) session.getAttribute("username")).id);
    }


    @DeleteMapping("/deletePublic/{idp}")
    RedirectView deletePublicPost(@PathVariable int idp ,Model model){
        posterRepository.deleteById(idp);
//        model.addAttribute("condition",);
        return new RedirectView("/publicPost");
    }
}
