package de.bund.bva.isyfact.common.web.filetransfer;

import java.io.ByteArrayInputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriUtils;

import de.bund.bva.isyfact.common.web.global.GlobalFlowController;
import de.bund.bva.isyfact.common.web.konstanten.FehlerSchluessel;
import de.bund.bva.isyfact.util.spring.MessageSourceHolder;

/**
 * @author msg
 */
@Component
public class DownloadHelper {

    @Autowired
    private GlobalFlowController globalFlowController;

    @Autowired
    private ZipHelper zipHelper;

    /**
     * Starts the download of a file with corresponding name and content.
     * In the event of an error, a message is automatically issued.
     *
     * @param fileModel the {@link FileModel} to be downloaded
     * @return true, if successful
     */
    public boolean starteDownload(FileModel fileModel) {

        final byte[] inhalt = fileModel.getInhalt();
        final String dateiname = fileModel.getDateiname();

        try {
            Faces.sendFile(FacesContext.getCurrentInstance(), new ByteArrayInputStream(inhalt), dateiname, inhalt.length, true);
            return true;
        } catch (IOException e) {
            globalFlowController.getMessageController().writeErrorMessage(
                MessageSourceHolder.getMessage(FehlerSchluessel.FEHLERTEXT_GUI_TECHNISCH_UEBERSCHRIFT),
                            MessageSourceHolder.getMessage(
                                            FehlerSchluessel.DOWNLOAD_WAR_NICHT_ERFOLGREICH,
                                            dateiname));
        }
        return false;
    }

    /**
     * Starts the download of a file with corresponding name and content.
     * In case of an error, a message is issued automatically.
     *
     * @param dokumenteDatenModel @{@link DokumentDatenModel} of the file to be downloaded.
     *                            The included filename is considered to have no extension.
     * @return true, if successful
     */
    public boolean starteDownload(DokumentDatenModel dokumenteDatenModel) {

        final byte[] inhalt = dokumenteDatenModel.getInhalt();
        final String dateiname =
            dokumenteDatenModel.getDateiname() + dokumenteDatenModel.getDateityp().getDateiErweiterung();

        try {
            Faces.sendFile(FacesContext.getCurrentInstance(), new ByteArrayInputStream(inhalt), dateiname, inhalt.length, true);
            return true;
        } catch (IOException e) {
            globalFlowController.getMessageController().writeErrorMessage(
                MessageSourceHolder.getMessage(FehlerSchluessel.FEHLERTEXT_GUI_TECHNISCH_UEBERSCHRIFT),
                MessageSourceHolder.getMessage(
                    FehlerSchluessel.DOWNLOAD_WAR_NICHT_ERFOLGREICH,
                    dateiname));
        }
        return false;
    }

    /**
     * Starts the download of a zip file with a corresponding name
     * and a list of files defined by {@link FileModel}s.
     * In case of an error, a message is issued automatically.
     *
     * @param dateiname the name of the file, incl. extension
     * @param dateien   arbitrary long list of files in form of {@link FileModel}s
     * @return <code>true</code>, if successful, else <code>false</code>
     */
    public boolean downloadZipFileByModel(String dateiname, FileModel... dateien) {

        boolean downloadErgebnis = true;

        try {
            final byte[] zipped = zipHelper.createZip(dateien);

            final FileModel zipFileModel = new FileModel();

            zipFileModel.setDateiname(dateiname);
            zipFileModel.setInhalt(zipped);
            zipFileModel.setMimeType(DateitypEnum.ZIP.getMimeType());

            downloadErgebnis = starteDownload(zipFileModel);

        } catch (IOException e) {
            globalFlowController.getMessageController().writeErrorMessage(
                MessageSourceHolder.getMessage(FehlerSchluessel.FEHLERTEXT_GUI_TECHNISCH_UEBERSCHRIFT),
                MessageSourceHolder.getMessage(
                                FehlerSchluessel.DOWNLOAD_WAR_NICHT_ERFOLGREICH,
                                dateiname));
        }

        return downloadErgebnis;
    }


    /**
     * <p>
     * Copy from OmniFaces.<br/>
     * See also:
     * </p>
     *
     * <pre>
     * org.omnifaces.util.Faces
     * org.omnifaces.util.LocalFaces
     * </pre>
     *
     * @author msg
     */
    private static final class Faces {


        private static final String DEFAULT_MIME_TYPE = "application/octet-stream";
        private static final int DEFAULT_SENDFILE_BUFFER_SIZE = 10240;
        private static final int DEFAULT_STREAM_BUFFER_SIZE = 10240;

        private static final String ERROR_UNSUPPORTED_ENCODING =
            "UTF-8 is apparently not supported on this platform.";

        private static final String SENDFILE_HEADER = "%s;filename=\"%2$s\"; filename*=UTF-8''%2$s";
        private static final Charset UTF_8 = StandardCharsets.UTF_8;

        private Faces() {
            // prevent instantiation
        }

        /**
         * Check if the given resource is not <code>null</code> and then close it, whereby any caught
         * {@link IOException} is been returned instead of thrown, so that the caller can if necessary handle
         * (log) or just ignore it without the need to put another try-catch.
         *
         * @param resource The closeable resource to be closed.
         * @return The caught {@link IOException}, or <code>null</code> if none is been thrown.
         */
        private static IOException close(Closeable resource) {

            if (resource != null) {
                try {
                    resource.close();
                } catch (IOException e) {
                    return e;
                }
            }

            return null;
        }

        /**
         * URL-encode the given string using UTF-8.
         *
         * @param string The string to be URL-encoded using UTF-8.
         * @return The given string, URL-encoded using UTF-8, or {@code null} if {@code null} was given.
         * @since 1.4
         */
        private static String encodeURL(String string) {
            return UriUtils.encodePath(string, UTF_8);
        }

        private static String getMimeType(FacesContext context, String name) {

            String mimeType = context.getExternalContext().getMimeType(name);

            if (mimeType == null) {
                mimeType = DEFAULT_MIME_TYPE;
            }

            return mimeType;
        }

        /**
         * Internal global method to send the given input stream to the response.
         *
         * @param context       the faces context
         * @param input         The file content as input stream.
         * @param filename      The file name which should appear in content disposition header.
         * @param contentLength The content length, or -1 if it is unknown.
         * @param attachment    Whether the file should be provided as attachment, or just inline.
         * @throws IOException Whenever something fails at I/O level. The caller should preferably not catch
         *                     it, but just redeclare it in the action method. The servletcontainer will handle it.
         */
        public static void sendFile(FacesContext context, InputStream input, String filename,
            long contentLength, boolean attachment) throws IOException {

            ExternalContext externalContext = context.getExternalContext();

            // Prepare the response and set the necessary headers.
            externalContext.setResponseBufferSize(DEFAULT_SENDFILE_BUFFER_SIZE);
            externalContext.setResponseContentType(getMimeType(context, filename));
            externalContext.setResponseHeader("Content-Disposition",
                String.format(SENDFILE_HEADER, attachment ? "attachment" : "inline", encodeURL(filename)));

            // Not exactly mandatory, but this fixes at least a MSIE quirk:
            // http://support.microsoft.com/kb/316431
            if (((HttpServletRequest) externalContext.getRequest()).isSecure()) {
                externalContext.setResponseHeader("Cache-Control", "public");
                externalContext.setResponseHeader("Pragma", "public");
            }

            // If content length is known, set it. Note that setResponseContentLength() cannot be used as it
            // takes
            // only int.
            if (contentLength != -1) {
                externalContext.setResponseHeader("Content-Length", String.valueOf(contentLength));
            }

            long size = stream(input, externalContext.getResponseOutputStream());

            // This may be on time for files smaller than the default buffer size, but is otherwise ignored
            // anyway.
            if (contentLength == -1) {
                externalContext.setResponseHeader("Content-Length", String.valueOf(size));
            }

            context.responseComplete();
        }

        /**
         * Stream the given input to the given output via a directly allocated NIO {@link ByteBuffer}. Both
         * the input and output streams will implicitly be closed after streaming, regardless of whether an
         * exception is been thrown or not.
         *
         * @param input  The input stream.
         * @param output The output stream.
         * @return The length of the written bytes.
         * @throws IOException When an I/O error occurs.
         */
        private static long stream(InputStream input, OutputStream output) throws IOException {

            ReadableByteChannel inputChannel = null;
            WritableByteChannel outputChannel = null;

            try {
                inputChannel = Channels.newChannel(input);
                outputChannel = Channels.newChannel(output);
                ByteBuffer buffer = ByteBuffer.allocateDirect(DEFAULT_STREAM_BUFFER_SIZE);
                long size = 0;

                while (inputChannel.read(buffer) != -1) {
                    buffer.flip();
                    size += outputChannel.write(buffer);
                    buffer.clear();
                }

                return size;
            } finally {
                close(outputChannel);
                close(inputChannel);
            }
        }
    }
}
