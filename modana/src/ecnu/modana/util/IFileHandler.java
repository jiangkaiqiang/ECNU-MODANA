package ecnu.modana.util;

import java.io.File;

/**
 * Interface to handle file as needed, used together with DirVisitor
 * @author cb
 */
public interface IFileHandler {

	/**
	 * specific handling process
	 * @param target target File to handle
	 */
	public void handle(File target);
}
