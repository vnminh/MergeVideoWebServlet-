package model.bean;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import model.BO.UpVideoToHDManagerBO;

public class UpVideoToHDHandler implements Runnable {

	private static final String ffmpegPath = "D:\\SoftWare\\FFMPEG\\ffmpeg-7.1-full_build\\ffmpeg-7.1-full_build\\bin\\ffmpeg.exe";
	private static final String rootFolder = "D:\\MinhDUT\\KY 5, NAM 24-25\\Lap trinh mang\\Source";
	private String inFilePath, outFilePath;
	private double status;
	private List<String> listPath = new ArrayList<String>();
	public UpVideoToHDHandler(String userID, int pID) {
		// TODO Auto-generated constructor stub
		inFilePath = rootFolder+File.separator+userID+File.separator+pID+File.separator+"output"+File.separator+"output.mp4";
		outFilePath = rootFolder+File.separator+userID+File.separator+pID+File.separator+"output"+File.separator+"output_HD.mp4";
		status = 0;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
            // Get video duration
            double duration = getVideoDuration(inFilePath);
            System.out.println(String.format("--------Duration: %.2f------------", duration));
            if (duration <= 0) {
                System.err.println("Failed to retrieve video duration.");
                return;
            }
            
            // Start FFmpeg process
            ProcessBuilder processBuilder = new ProcessBuilder(ffmpegPath,
            												   "-i",
            												   inFilePath,
            												   "-vf",
            												   "scale=1920:1080:flags=lanczos",
            												   "-c:v",
            												   "libx264",
            												   "-preset",
            												   "fast",
            												   "-crf",
            												   "23",
            												   "-c:a",
            												   "copy",
            												   outFilePath);
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();

            // Monitor progress
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                Pattern timePattern = Pattern.compile("time=(\\d{2}):(\\d{2}):(\\d{2}\\.\\d{2})");
                while ((line = reader.readLine()) != null) {
                    System.out.println(line); // For debugging
                    Matcher matcher = timePattern.matcher(line);
                    if (matcher.find()) {
                        double currentTime = convertTimeToSeconds(matcher.group(1), matcher.group(2), matcher.group(3));
                        status = ((currentTime / duration) * 100);
                        System.out.printf("------------------Progress to HD: %.2f%%-----------------------\n", status); // Console progress bar
                    }
                }
            }

            int exitCode = process.waitFor();
            if (exitCode == 0) {
                System.out.println("\nFFmpeg process completed successfully.");
            } else {
                System.err.println("\nFFmpeg process failed.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
	
	
	private double getVideoDuration(String filePath) throws Exception {
		ProcessBuilder processBuilder = new ProcessBuilder(ffmpegPath, "-i", filePath);
		processBuilder.redirectErrorStream(true);
		Process process = processBuilder.start();
		
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
			String line;
			Pattern durationPattern = Pattern.compile("Duration: (\\d{2}):(\\d{2}):(\\d{2}\\.\\d{2})");
			while ((line = reader.readLine()) != null) {
				System.out.println(line);
				Matcher matcher = durationPattern.matcher(line);
				if (matcher.find()) {
					return convertTimeToSeconds(matcher.group(1), matcher.group(2), matcher.group(3));
				}
			}
		}
		process.waitFor();
		return 0;
    }
	
    private double convertTimeToSeconds(String hours, String minutes, String seconds) {
        return Integer.parseInt(hours) * 3600 + Integer.parseInt(minutes) * 60 + Double.parseDouble(seconds);
    }

    private List<String> extractListPath(String fileListPath){
    	try {
			List<String> list = new ArrayList<String>();
			Scanner scn = new Scanner(new File(fileListPath));
			while (scn.hasNext()) {
				String line = scn.nextLine();
				String path = line.substring(line.indexOf("'")+1,line.length()-1);
				list.add(path);
			}
			scn.close();
			return list;
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return null;
    }

	public double getStatus() {
		return status;
	}
}
