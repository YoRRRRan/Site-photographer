package upload;

import java.io.File;
import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;



/*
 * Notre serlvet permettant de r�cup�rer les fichiers c�t� serveur.
 * Elle r�pondra � l'URL /upload dans l'application Web consid�r�e.
 */
@WebServlet( urlPatterns = "/upload" )
@MultipartConfig( fileSizeThreshold = 1024 * 1024, 
                  maxFileSize = 1024 * 1024 * 5,
                  maxRequestSize = 1024 * 1024 * 5 * 5 )
public class SavePhoto extends HttpServlet {

    private static final long serialVersionUID = 1273074928096412095L;
    
    /*
     * Chemin dans lequel les images seront sauvegard�es.
     */
    public static final String IMAGES_FOLDER = "/Images";
        
    public String uploadPath;
    
    /*
     * Si le dossier de sauvegarde de l'image n'existe pas, on demande sa cr�ation.
     */ 
    @Override
    public void init() throws ServletException {
        uploadPath = getServletContext().getRealPath( IMAGES_FOLDER );
        File uploadDir = new File( uploadPath );
        if ( ! uploadDir.exists() ) uploadDir.mkdir();
    }
       
    /*
     * R�cup�ration et sauvegarde du contenu de chaque image.
     */ 
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse resp)
            throws ServletException, IOException {
        for ( Part part : request.getParts() ) {
            String fileName = getFileName( part );
            String fullPath = uploadPath + File.separator + fileName;
            part.write( fullPath );
        }
    }

    /*
     * R�cup�ration du nom du fichier dans la requ�te.
     */
    private String getFileName( Part part ) {
        for ( String content : part.getHeader( "content-disposition" ).split( ";" ) ) {
            if ( content.trim().startsWith( "filename" ) )
                return content.substring( content.indexOf( "=" ) + 2, content.length() - 1 );
        }
        return "Default.file";
    }

}