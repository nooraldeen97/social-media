package com.example.auth14.web;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;

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
//            model.addAttribute("userData",session.getAttribute("username"));
            model.addAttribute("user",chatUserRepository.findByUsername(session.getAttribute("username").toString()));
            List<Poster> posters=posterRepository.findAllByChatUserUsername((String) session.getAttribute("username"));
            model.addAttribute("imgURL",chatUserRepository.findByUsername(session.getAttribute("username").toString()).getPhotosImagePath());
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

       List<Poster>posters= posterRepository.findAllByIsPublic(true);

        model.addAttribute("current",request.getSession().getAttribute("username"));
        model.addAttribute("posters",posters);
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

    @PutMapping("/users/save")
    RedirectView addImage(HttpServletRequest request , @RequestParam("image") MultipartFile multipartFile) throws IOException {
        ChatUser chatUser=chatUserRepository.findByUsername(request.getSession().getAttribute("username").toString());

        String fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        chatUser.setImage(fileName);

        ChatUser savedUser = chatUserRepository.save(chatUser);

        String uploadDir = "user-photos/" + savedUser.getId();

        FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);

        return new RedirectView("/");
    }
}
