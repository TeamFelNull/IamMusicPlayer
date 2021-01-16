package red.felnull.imp.ffmpeg;

import net.minecraftforge.fml.ModList;
import red.felnull.imp.exception.IMPFFmpegException;
import red.felnull.imp.util.PathUtils;
import red.felnull.otyacraftengine.util.IKSGFileLoadUtil;
import red.felnull.otyacraftengine.util.IKSGModUtil;

import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FFmpegErrorReporter {
    public static void reportExport(Exception ex, IMPFFmpegException impex) {
        IKSGFileLoadUtil.createFolder(PathUtils.getFFmpegReportFolder());
        Date date = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-mm-dd_HH.mm.ss");
        Path pt = PathUtils.getFFmpegReportFolder().resolve("error-" + df.format(date) + ".txt");
        FFmpegManeger maneger = FFmpegManeger.instance();
        StringBuffer sb = new StringBuffer();
        sb.append("---- FFmpeg Error Report ----\n");
        SimpleDateFormat dff = new SimpleDateFormat("yy/mm/dd HH:mm");
        sb.append("Time: ").append(dff.format(date)).append("\n");
        sb.append("Description: ").append(ex.getLocalizedMessage()).append("\n");
        sb.append("IMPDescription: ").append(impex.getLocalizedMessage()).append("\n\n");
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        sb.append(sw.toString()).append("\n\n");
        StringWriter impsw = new StringWriter();
        PrintWriter imppw = new PrintWriter(sw);
        ex.printStackTrace(imppw);
        sb.append(impsw.toString()).append("\n");
        sb.append("Info:").append("\n");
        sb.append("---------------------------------------------------------------------------------------").append("\n\n");
        sb.append("-- Head --").append("\n");
        Thread cr = Thread.currentThread();
        sb.append("Thread: ").append(cr.getName()).append("\n");
        sb.append("Stacktrace:\n");
        for (StackTraceElement ste : cr.getStackTrace()) {
            sb.append(ste.toString()).append("\n");
        }
        sb.append("\n\n");
        sb.append("-- System Details --").append("\n");
        sb.append("Details:").append("\n");
        String os = System.getProperty("os.name");
        String arch = System.getProperty("os.arch");
        sb.append("OS and Arch: ").append(os).append(" - ").append(arch).append(" - ").append(maneger.getOsAndArch().getName()).append("\n");
        sb.append("Java: ").append(System.getProperty("java.vm.vendor")).append(" ").append(System.getProperty("java.vm.name")).append(" ").append(System.getProperty("java.version")).append("\n");

        InputStream ffmpegResource = maneger.getFFmpegResource(maneger.getOsAndArch().getResourceName());
        sb.append("FFmpeg Included: ").append(ffmpegResource != null).append("\n\n");

        sb.append("-- Mod List --").append("\n");
        ModList.get().getMods().forEach(n -> {
            sb.append(n.getModId()).append(": ").append(IKSGModUtil.getModVersion(n.getModId())).append("\n");
        });

        IKSGFileLoadUtil.txtWriter(sb.toString(), pt);
    }
}
