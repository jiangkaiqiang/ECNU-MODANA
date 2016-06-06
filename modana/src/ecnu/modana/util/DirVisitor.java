package ecnu.modana.util;

import java.io.File;

import org.apache.log4j.Logger;

/**
 * visit all files in root directory, used together with IFileHnadler to handle each visited file
 * @author cb
 */
public class DirVisitor {
	
	Logger logger = Logger.getRootLogger();
	
	IFileHandler fileHandler;
	
	/**
	 * constructor
	 * @param fileHandler specify the handling process for each file
	 */
	public DirVisitor(IFileHandler fileHandler) {
		this.fileHandler = fileHandler;
	}
	
	/**
	 * visit all files and directories
	 * @param pathStr root directory path string or a file
	 * @param visitSubdir whether to visit sub-directories
	 */
	public void visitAll(String pathStr, boolean visitSubdir) throws Exception {
		File root = new File(pathStr);
		if (root.exists()) {
			if (root.isDirectory()) { //for directory
				visitDir(root, visitSubdir);
			} else {
				fileHandler.handle(root); //do something as defined
			}
		} else {
			logger.error("Path (" + pathStr + ") does not exist!");
		}
	
	}

	private void visitDir(File dir, boolean visitSubdir) {
		File files[] = dir.listFiles();
		for (File f : files) {
			if (visitSubdir && f.isDirectory()) {
				//recursively visit sub directories if marked as visitSubdir=true
				visitDir(f, visitSubdir);
			} else {
				fileHandler.handle(f);
			}
		}

	}
	
}
