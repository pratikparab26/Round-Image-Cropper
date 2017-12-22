package com.pratik.roundImgCropper;

import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
 * Servlet implementation class CropImageRound
 */
@WebServlet("/CropImageRound")
public class CropImageRound extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	private static final String UPLOAD_DIRECTORY = "upload";
	private static final int THRESHOLD_SIZE     = 1024 * 1024 * 3;  // 3MB
	private static final int MAX_FILE_SIZE      = 1024 * 1024 * 40; // 40MB
	private static final int MAX_REQUEST_SIZE   = 1024 * 1024 * 50; // 50MB
	 String fileName="";
	
    /**
     * 
     * @see HttpServlet#HttpServlet()
     */
    public CropImageRound() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		
		if (!ServletFileUpload.isMultipartContent(request)) {
		    PrintWriter writer = response.getWriter();
		    writer.println("Request does not contain upload data");
		    writer.flush();
		    return;
		}
		// configures upload settings
		DiskFileItemFactory factory = new DiskFileItemFactory();
		factory.setSizeThreshold(THRESHOLD_SIZE);
		factory.setRepository(new File(System.getProperty("java.io.tmpdir")));
		 
		ServletFileUpload upload = new ServletFileUpload(factory);
		upload.setFileSizeMax(MAX_FILE_SIZE);
		upload.setSizeMax(MAX_REQUEST_SIZE);
		
		// constructs the directory path to store upload file
		String uploadPath = getServletContext().getRealPath("")
		    + File.separator + UPLOAD_DIRECTORY;
		uploadPath= "D:\\"+UPLOAD_DIRECTORY;
		// creates the directory if it does not exist
		File uploadDir = new File(uploadPath);
		if (!uploadDir.exists()) {
		    uploadDir.mkdir();
		}
		
		List formItems = null;
		try {
			formItems = upload.parseRequest(request);
		} catch (FileUploadException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Iterator iter = formItems.iterator();
		 
		// iterates over form's fields
		while (iter.hasNext()) {
		    FileItem item = (FileItem) iter.next();
		    // processes only fields that are not form fields
		    if (!item.isFormField()) {
		         fileName = new File(item.getName()).getName();
		        String filePath = uploadPath + File.separator + fileName;
		        System.out.println(uploadPath);
		        File storeFile = new File(filePath);
		 
		        // saves the file on disk
		        try {
					item.write(storeFile);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		        
		        
		        
		        
		    }
		}
		
	//crop image round
		File imageFile = new File(uploadPath + File.separator + fileName); //image file path
		System.out.println(imageFile.getAbsolutePath());
		BufferedImage inputimage = ImageIO.read(imageFile);
		BufferedImage dst = new BufferedImage(inputimage.getWidth(), inputimage.getHeight(),BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = dst.createGraphics();
		 g.setClip(new Ellipse2D.Float(0, 0,inputimage.getWidth(), inputimage.getHeight()));
		    g.drawImage(inputimage, 0, 0,inputimage.getWidth(), inputimage.getHeight(), null);
		    g.dispose();
			try{
			
				File outputfile = new File(uploadPath + File.separator +"circle.png");
				ImageIO.write(dst, "png", outputfile);
				
			}catch(Exception e){
				System.out.println(e);
				
			}
			  getServletContext().getRequestDispatcher("/showProfilePic.html").forward(request, response);
	}

}
