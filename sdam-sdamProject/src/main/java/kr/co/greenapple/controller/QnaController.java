package kr.co.greenapple.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import kr.co.greenapple.beans.PageBean;
import kr.co.greenapple.beans.QnaBean;
import kr.co.greenapple.beans.UserBean;
import kr.co.greenapple.service.QnaService;

@Controller
@RequestMapping("/customer")
public class QnaController {
	
	@Autowired
	private QnaService qnaService;
	
	@Resource(name = "loginUserBean")
	@Lazy
	private UserBean loginUserBean;	

	@GetMapping("/qna")
	public String main(@ModelAttribute QnaBean qnaBean,
//			@RequestParam(value = "page", defaultValue = "1") int page,
			Model model) {
		
		List<QnaBean> qnaList = qnaService.getQnaList(qnaBean);
		model.addAttribute("qnaList", qnaList);
		
//		List<Object> pageBean = qnaService.getQnaCnt(page);
//		model.addAttribute("pageBean", pageBean);
//		model.addAttribute("page", page);
		
		return "customer/qna_main";
	}

	@GetMapping("/read")
	public String read(QnaBean qnaBean,
			@RequestParam("qna_idx") int qna_idx,
//			@RequestParam("page") int page,
			Model model) {
		
		model.addAttribute("readQnaBean", qnaBean);
		
		QnaBean readQnaBean = qnaService.getQnaInfo(qna_idx);
		model.addAttribute("readQnaBean", readQnaBean);
		model.addAttribute("loginUserBean", loginUserBean);
		
//		model.addAttribute("page", page);
		
		return "customer/qna_read";
	}

	@GetMapping("/write")
	public String write(@ModelAttribute("writeQnaBean") QnaBean writeQnaBean,
			Model model) {
		
		System.out.println(loginUserBean.getUser_id());
//		writeQnaBean.setContent_board_idx(board_info_idx);
		if(loginUserBean.getUser_id() == null) {
			return "user/not_login";
		}

		model.addAttribute("loginUserBean", loginUserBean);
		
		return "customer/qna_write";
	}
	
	@PostMapping("/write_pro")
	public String write_pro(@Valid @ModelAttribute("writeQnaBean") QnaBean writeQnaBean, BindingResult result) {
		if(result.hasErrors()) {
			return "customer/qna_write";
		}
		
		qnaService.addQnaContentInfo(writeQnaBean);
		
		return "customer/qna_write_success";
	}

	@GetMapping("/modify")
	public String modify(
//			@RequestParam("qna_idx") int qna_idx,
			@ModelAttribute("modifyQnaBean") QnaBean modifyQnaBean,
//			@RequestParam("page") int page,
			Model model) {
		
//		model.addAttribute("qna_idx", qna_idx);
		QnaBean ModifyQnaBean = qnaService.getQnaContentInfo(modifyQnaBean.getQna_idx());
		
		modifyQnaBean.setWriter_name(ModifyQnaBean.getWriter_name());
		modifyQnaBean.setQna_date(ModifyQnaBean.getQna_date());
		modifyQnaBean.setQna_subject(ModifyQnaBean.getQna_subject());
		modifyQnaBean.setQna_content(ModifyQnaBean.getQna_content());
		modifyQnaBean.setContent_file(ModifyQnaBean.getContent_file());
		modifyQnaBean.setUser_idx(ModifyQnaBean.getUser_idx());
//		modifyQnaBean.setQna_idx(qna_idx);
		
//		model.addAttribute("page", page);
				
		return "customer/qna_modify";
	}
	
	@PostMapping("/modify_pro")
	public String modify_pro(@Valid @ModelAttribute("modifyQnaBean") QnaBean modifyQnaBean, BindingResult result,
			@RequestParam("page") int page, Model model) {
		
		model.addAttribute("page", page);
		
		if(result.hasErrors()) {
			return "customer/qna_modify";
		}
				
		qnaService.modifyQnaContentInfo(modifyQnaBean);
		
		return "customer/qna_modify_success";
	}

	@GetMapping("/delete")
	public String delete(@RequestParam("qna_idx") int qna_idx,
			Model model) {
		
		qnaService.deleteQnaContentInfo(qna_idx);
		
		return "customer/qna_delete";
	}
	
	@GetMapping("/not_writer") 
	public String not_writer() {	
		return "customer/qna_not_writer";
	}
}
