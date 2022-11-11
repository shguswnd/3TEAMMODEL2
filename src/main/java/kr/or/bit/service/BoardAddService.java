package kr.or.bit.service;

import java.util.Enumeration;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

import kr.or.bit.action.Action;
import kr.or.bit.action.ActionForward;
import kr.or.bit.dao.BoardDao;
import kr.or.bit.dto.Board;

public class BoardAddService implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) {
		String subject = "";
		String writer = "";
		String email = "";
		String homepage = "";
		String content = "";
		String pwd = "";
		String filename = "";
		HttpSession session = request.getSession();
		int size = 1024*1024*10;
		String root = session.getServletContext().getRealPath("/");
		System.out.println(root);
		String savepath = "upload";
		String uploadpath = root+ savepath;
		
		int result = 0;

		
		
		
		

		try {
			
			try {
				MultipartRequest multi = new MultipartRequest(
						request, 	
						uploadpath, 	
						size, 
						"UTF-8",
						new DefaultFileRenamePolicy()
					);
				Enumeration filenames = multi.getFileNames();
				subject = multi.getParameter("subject");
				writer = multi.getParameter("writer");
				email = multi.getParameter("email");
				homepage = multi.getParameter("homepage");
				content = multi.getParameter("content");
				pwd = multi.getParameter("pwd");
				filename = multi.getParameter("filename");
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			Board board = new Board();
			
			board.setSubject(subject);
			board.setWriter(writer);
			board.setEmail(email);
			board.setHomepage(homepage);
			board.setContent(content);
			board.setPwd(pwd);
			board.setFilename(filename);
			
			BoardDao dao = new BoardDao();
			result = dao.writeok(board);
		} catch (NamingException e) {
			e.printStackTrace();
		}
		

		// write.jsp 화면 >> writeok.jsp 처리 >> service >> dao > DB 작업 >
		// return dao > return service > writeok.jsp 결과처리 >> 이동 (공통) >> redirect.jsp

		String msg = "";
		String url = "";
		if (result > 0) {
			msg = "insert success";
			url = "BoardList.do";
		} else {
			msg = "insert fail";
			url = "BoardWrite.do";
		}

		request.setAttribute("board_msg", msg);
		request.setAttribute("board_url", url);

		ActionForward forward = new ActionForward();
		forward.setRedirect(false);
		forward.setPath("/WEB-INF/views/board/redirect.jsp");

		return forward;

	}

}
