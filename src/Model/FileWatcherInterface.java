package Model;

import java.io.File;

public interface FileWatcherInterface {

    public void PrintListFiles();

    public String[] ReturnFileNames();

    public File[] ReturnListOfFiles();

    public File ReturnFileReq(String name);
}
