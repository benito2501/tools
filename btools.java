/*
*Written by benito2501
*
* You can use the code however you want; would appreciate a mention.
*
 */
import java.io.*;
import java.util.logging.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Scanner;
public class btools{

	public static final String  VERSION = "0.7.1";
	public static final String[] LOG = {
			"btools was written by benito2501 on github",
			"6/27/2021 VERSION 0.2",
			"public static ArrayList<Path> extract_files(Path target, debugtool tool):",
			"solely extracts the path objects that return true for being files.",
			"adding an to this in the future for spefic file types may be valuable.",
			"6/29/2021",
			"I add a version of extraction that gets all the content inside a directory.",
			"6/30/2021 VERSION 0.3",
			"public static String hasStr(String input, String[] cotent)",
			"public static boolean has(String input, String[] content)",
			"now you can have an array of content to search as opposed to one item.",
			"12/15/2021 VERSION 0.4",
			"got rid of the debug tool entries so that btools can act standalone with the basic java packages",
			"2/2/2022 VERSION 0.5",
			"add the has_parameter command in order to extract an array of parameters after a command has been entered through text.",
			"2/11/2022 VERSION 0.5.1",
			"fixed the priority at which has_option would return an option; it returns at the first instance of an option",
			"2/16/2022 VERSION 0.5.2",
			"text prompt is deprecated",
			"4/12/2022 VERSION 0.6",
			"well the logger is finally in after doing some work; took a while since I was busy n' stuff.",
			"4/18/2022 VERSION 0.7",
			"overloaded has_parameter so that it can return just a string instead of a whole array",
			"5/25/2022 VERSION 0.7.1",
			"changed has_option",
			"changed has_parameter; if string 'command' is null, 'has_parameter' returns an array with just -1 inside"
	};

	private static final Logger btools_log = Logger.getLogger( btools.class.getName() );

	public static void bcopyf(File source, File location, boolean debug) throws FileNotFoundException, IOException, SecurityException{
		//please put bcopyf instead a try clause to catch IOExceptions that it throws
		File placement;
		if(location.getAbsolutePath().endsWith("\\"))
			placement = new File(location.getAbsolutePath() + source.getName());
		else
			placement = new File(location.getAbsolutePath() + "\\" + source.getName());

		FileInputStream fin = new FileInputStream(source);
		FileOutputStream fout = new FileOutputStream(placement);

		if(debug)
			System.out.println("Copying " + source.getName() + " into\n" + location.getAbsolutePath());

		byte[] buf = new byte[1024];
        int length;
        long total_bytes = 0;
        boolean[] total_flags = {false, false, false};
        while ((length = fin.read(buf)) > 0) {
            fout.write(buf, 0, length);
            total_bytes += (long) length;
            if(debug){
            	if(total_bytes >= (source.length() * .25) & total_flags[0] == false){
            		System.out.println(total_bytes + " of " + source.length() + " copied.");
            		total_flags[0] = true;
            	}
            	if(total_bytes >= (source.length() * .50) & total_flags[1] == false){
            		System.out.println(total_bytes + " of " + source.length() + " copied.");
            		total_flags[1] = true;
            	}
            	if(total_bytes >= (source.length() * .75) & total_flags[2] == false){
            		System.out.println(total_bytes + " of " + source.length() + " copied.");
            		total_flags[2] = true;
            	}
            } 
        }
        if(debug)
        	System.out.println("Finished copying " + source.getName() + " to " + location.getAbsolutePath());
        fin.close();
        fout.close();
	}

	public static void bcopyf(String source, String location, boolean debug) throws FileNotFoundException, IOException, SecurityException{
		File sauce = new File(source);
		File placement = new File(location);
		bcopyf(sauce, placement, debug);
		/*interesting observation: if you dont add // in a raw string conversion, you'll get
		a FileNotFoundException because java will think / is an escape sequence.*/
	}

	private static void recursive_dirs(File source, File location, boolean debug) throws FileNotFoundException, IOException, SecurityException{
		File placement;
		if(location.getAbsolutePath().endsWith("\\"))
			placement = new File(location.getAbsolutePath() + source.getName());
		else
			placement = new File(location.getAbsolutePath() + "\\" + source.getName());
		
		if(debug){
			System.out.println(source.getAbsolutePath() + "\nbeing copied into");
			System.out.println(location.getAbsolutePath());
		}
		File[] entrys = source.listFiles();
		for(File f : entrys){
			if(f.isDirectory()){
				if(debug){
					System.out.println(f.getAbsolutePath() + "\nbeing copied into");
					System.out.println("To " + placement.getAbsolutePath());
					System.in.read();
				}
				recursive_dirs(f, placement, debug);
			}
		}
		placement.mkdirs();
		for(File f : entrys){
			if(f.isFile())
				bcopyf(f, placement, debug);
		}
	}
	public static void bcopyd(File source, File location, boolean debug) throws FileNotFoundException, IOException, SecurityException{
		if(!source.exists()){
			FileNotFoundException n = new FileNotFoundException(source.getAbsolutePath() + " does not exist!");
			throw n;
		}
		if(!location.exists()){
			FileNotFoundException n = new FileNotFoundException(location.getAbsolutePath() + " does not exist!");
			throw n;
		}
		recursive_dirs(source, location, debug);
	}

	public static void bcopyd(String source, String location, boolean debug) throws FileNotFoundException, IOException, SecurityException{
		File sauce = new File(source);
		File placement = new File(location);
		bcopyd(sauce, placement, debug);
		/*interesting observation: if you dont add // in a raw string conversion, you'll get
		a FileNotFoundException because java will think / is an escape sequence.*/
	}

	public static boolean look(File source, File location)throws FileNotFoundException, IOException, SecurityException{
		File[] entrys_A = location.listFiles();
		for(File f : entrys_A){
			if(source.getName().equals(f.getName()) ){
				return true;
			}
		}
		return false;
	}

	public static boolean look(String source, String location)throws FileNotFoundException, IOException, SecurityException{
		File sauce = new File(source);
		File placement = new File(location);
		return look(sauce, placement);
	}

	private static void determine(File source, File location, boolean copym)throws FileNotFoundException, IOException, SecurityException{
		//1/11/2019 making it so recursion is assumed and using the recur boolean to determine if the user wants to copy to file over to location
		File[] entrys = source.listFiles();
		for(File e : entrys){
			if(look(e, location) ){
				System.out.println(e.getName() + "is in \n" + location.getAbsolutePath());
				if(e.isDirectory() ){
					System.out.println("Checking " + e.getAbsolutePath());
					File ment;
					if(location.getAbsolutePath().endsWith("\\"))
						ment = new File(location.getAbsolutePath() + e.getName());
					else
						ment = new File(location.getAbsolutePath() + "\\" + e.getName());
					determine(e, ment, copym);
				}
			}
			else{
				System.out.println(e.getName() + "\nis not in \n" + location.getAbsolutePath());
				System.out.println();
				if(copym ){
					if(e.isDirectory() )
						bcopyd(e, location, true);
					if(e.isFile() )
						bcopyf(e, location, true);
				}
			}
		}
	}

	public static int count_missing(File source, File location)throws FileNotFoundException, IOException, SecurityException{
		int missing = 0;
		File[] entrys = source.listFiles();
		for(File e : entrys){
			if(!look(e, location))
				missing += 1;
		}
		return missing;
	}

	public static int count_missing(String source, String location)throws FileNotFoundException, IOException, SecurityException{
		File sauce = new File(source);
		File placement = new File(location);
		return count_missing(sauce, placement);
	}

	public static File[] obtain_missing(File source, File location)throws FileNotFoundException, IOException, SecurityException{
		File[] entrys = source.listFiles();
		int count = count_missing(source, location);
		File[] missing = new File[count];
		count = 0;
		//use try here to catch nullpointers
		for(File f : entrys){
			if(!look(f, location)){
				missing[count] = f;
				count += 1;
			}
		}
		return missing;
	}

	public static boolean has(String input, String content){
		//in all iterations of has, input is the user string while
		//content is the string to be searched for
		btools_log.log(Level.FINE, "public static boolean has(input: "+input+" content: "+content+")");
		int count = 0;
		if (input.length() < content.length()){
			return false;
		}
		else{
			for (int i = 0; i < input.length(); i++){
				if (input.charAt(i) == content.charAt(count)){
					if (count == content.length()-1)
						return true;
					else
						count += 1;
				}
				else{
					count = 0;
				}
			}
		}
		return false;
	}

	public static String hasStr(String input, String content){
		int count = 0;
		if (input.length() < content.length()){
			return input;
		}
		else{
			for (int i = 0; i < input.length(); i++){
				if (input.charAt(i) == content.charAt(count)){
					if (count == content.length()-1)
						return content;
					else
						count += 1;
				}
				else{
					count = 0;
				}
			}
		}
		return input;
	}

	public static String hasStr(String input, String[] cotent){
		for(String tent: cotent){
			if(has(input, tent) )
				return tent;
		}
		return input;
	}

	public static boolean has(String[] inputs, String content){

		for(String input: inputs){
			if( has(input, content) )
				return true;
		}
		return false;
	}

	public static boolean has(String input, String[] content){
		//in all iterations of has, input is the user string while
		//content is the string to be searched for
		//and in this case, you have an amount of content to search for
		for(String tent: content){
			if( has(input, tent) )
				return true;
		}
		return false;
	}

	public static int has_option(String[] inputs, String content){
		//5/25/2022
		//ive decided to just start using 'matches' method from string class to determine validity
		//for now anyway
		for(int p=0;p< inputs.length;p++){
			if(has(inputs[p], content) )
				return p;
		}

		return -1;
	}

	public static int has_index(String input, String content, boolean side){
		//true returns the first index, false returns the last index
		int count = 0;
		int first_index = -1;
		if (input.length() < content.length()){
			return -1;
		}
		else{
			for (int i = 0; i < input.length(); i++){
				if (input.charAt(i) == content.charAt(count)){
					if (count == content.length()-1){
						if(side == true)
							return first_index;
						else
							return i;
					}
					else{
						count += 1;
						if(first_index == -1)
							first_index = i;
					}
				}
				else{
					count = 0;
					first_index = -1;
				}
			}
		}
		return -1;
	}

	public static ArrayList<Path> extract_files(Path target){
		String mname = "extract_files";
		//tool.addlog(mname+"("+target.toString()+")" );
		ArrayList<Path> product = new ArrayList<Path>(20);
		//tool.addlog("Addressing the files of "+target.toString() );
		//later on try and move the try clause up one level so that each program handles errors accordingly
		try (DirectoryStream<Path> target_stream = Files.newDirectoryStream(target) ){
			for (Path file: target_stream){
				if(file.toFile().isFile() ){
					product.add(file);
					//tool.addlog( (product.indexOf(file)+1)+". "+ file.toFile().getName() );
				}
			}
		} catch (IOException | DirectoryIteratorException x){
			//tool.display(x.toString() );
		}
		return product;
	}

	public static ArrayList<Path> extract_folders(Path target){
		String mname = "extract_folders";
		//tool.addlog(mname+"("+target.toString()+")" );
		ArrayList<Path> product = new ArrayList<Path>(20);
		//tool.addlog("Addressing the folders of "+target.toString() );
		//later on try and move the try clause up one level so that each program handles errors accordingly
		try (DirectoryStream<Path> target_stream = Files.newDirectoryStream(target) ){
			for (Path file: target_stream){
				if(file.toFile().isDirectory() ){
					product.add(file);
					//tool.addlog( (product.indexOf(file)+1)+". "+ file.toFile().getName() );
				}
			}
		} catch (IOException | DirectoryIteratorException x){
			//tool.display(x.toString() );
		}
		return product;
	}

	public static ArrayList<Path> extract_all(Path target){
		String mname = "extract_all";
		//tool.addlog(mname+"("+target.toString()+")" );
		ArrayList<Path> product = new ArrayList<Path>(20);
		//tool.addlog("Addressing the contents of "+target.toString() );
		//later on try and move the try clause up one level so that each program handles errors accordingly
		try (DirectoryStream<Path> target_stream = Files.newDirectoryStream(target) ){
			for (Path file: target_stream){
				product.add(file);
				//tool.addlog( (product.indexOf(file)+1)+". "+ file.toFile().getName() );
			}
		} catch (IOException | DirectoryIteratorException x){
			//tool.display(x.toString() );
		}
		return product;
	}


	public static String[] has_parameter(String command, String entry){
		//2/3/2022
		//so to reiterate, you input the main command like the name of function such as mkdir
		//or shutdown followed by -s, -d, or whatever parameter you wish. All the parameters are
		//divided by a space, but if you need to input a parameter with spaces for some reason,
		//surround the parameter with quotes. "This would be a parameter". Keep in mind that the
		//function starts to obtain a parameter when it reads a quotation mark and ends the input
		//when it finds another quotation mark. If the function does not find an end quotation mark
		//it will continue to construct a parameter until the end of the input.
		//5/25/2022
		//if string 'command' is null, 'has_parameter' returns an array with just -1 inside
		ArrayList<String>product = new ArrayList<String>(5);
		StringBuffer buf = new StringBuffer();
		int point;
		if(command == null)
			return new String[] {"-1"};
		else if(command.contains(" ") | command.contains("\"") )
			return new String[] {"-1"};
		if(command.matches("-1") | entry.indexOf(command) == -1)
			point = 0;
		else
			point = entry.indexOf(command) + command.length();
		boolean q = false;
		for(;point < entry.length(); point++){
			if(entry.charAt(point) == '\"'){
				if(!q)
					q = true;
				else {
					q = false;
					if(buf.length() > 0){
						product.add(buf.toString() );
						buf = new StringBuffer();
					}
				}
			}
			else{
				if(entry.charAt(point) != ' '){
					buf.append(entry.charAt(point) );
					if(point == entry.length() - 1 )
						product.add(buf.toString() );
				}
				else{
					if(q){
						buf.append(entry.charAt(point) );
					}
					else{
						if(buf.length() > 0){
							product.add(buf.toString() );
							buf = new StringBuffer();
						}
					}
				}
			}
		}
		if(product.size() == 0){
			return new String[] {command};
		}
		else{
			return product.toArray(new String[] {});
		}
	}

	public static String has_parameter(String command, String entry, int n){
		String[] cells = has_parameter(command, entry);
		if(n < 0)
			return cells[0];
		if(n > cells.length)
			return cells[cells.length - 1];
		return cells[n];
	}

	public static boolean addhandle(Handler h){
		if(btools_log != null) {
			btools_log.addHandler(h);
			btools_log.severe("btools was written by benito2501 on github");
		}
		else
			return false;
		return true;
	}

	public static void publish_log(){
		if(btools_log != null) {
			for (Handler h : btools_log.getHandlers())
				h.flush();
		}
	}

	public static boolean setlog_level(Level l){
		if(btools_log != null)
			btools_log.setLevel(l);
		else
			return false;
		return true;
	}

	public static void main(String args[]){
		System.out.println("Welcome to the btools command line!");
		Scanner reader = new Scanner(System.in);
		if(args.length != 0){
			switch(args[0]){
				case "look":
					try{
						System.out.println("Searching for " + args[1]);
						System.out.println("in " + args[2]);
						if(look(args[1], args[2]) )
							System.out.println("The directory exists!");
						else
							System.out.println("The directory does not exist!");
					}
					catch(ArrayIndexOutOfBoundsException e){
						System.out.println("look needs a source and location");
						e.printStackTrace();
					}
					catch(SecurityException e){
						e.printStackTrace();
					}
					catch(FileNotFoundException f){
						f.printStackTrace();
					}
					catch(IOException e){
						e.printStackTrace();
					}
					break;
				case "copy":
					try{
						bcopyf(args[1], args[2], true);
					}
					catch(ArrayIndexOutOfBoundsException e){
						System.out.println("copy needs a source and location.");
						e.printStackTrace();
					}
					catch(SecurityException e){
						e.printStackTrace();
					}
					catch(FileNotFoundException f){
						f.printStackTrace();
					}
					catch(IOException e){
						e.printStackTrace();
					}
					break;
				case"copyd":
					try{
						bcopyd(args[1], args[2], true);
					}
					catch(ArrayIndexOutOfBoundsException e){
						System.out.println("copy needs a source and location.");
						e.printStackTrace();
					}
					catch(SecurityException e){
						e.printStackTrace();
					}
					catch(FileNotFoundException f){
						f.printStackTrace();
					}
					catch(IOException e){
						e.printStackTrace();
					}
					break;
				case "chk":
					try{
						File test = new File(args[1]);
						if(test.exists()){
							if(test.isFile())
								System.out.println(test.toString() + " is a file.");
							if(test.isDirectory())
								System.out.println(test.toString() + " is a directory.");
						}
						else
							System.out.println("This directory does not exist.");
					}
					catch(ArrayIndexOutOfBoundsException e){
						System.out.println("No directory was enetered with chk.");
						e.printStackTrace();
					}
					break;
				case "determine":
					boolean r = false;
					char i;
					System.out.println("Copy over missing files? y/n");
					try{
						i = (char) System.in.read();
						if(i == 'y' | i == 'Y')
							r = true;
					}
					catch(IOException e){
						System.out.println(e);
						break;
					}
					try{
						File source = new File(args[1]);
						File location = new File(args[2]);
						determine(source, location, r);
						//match(args[1], args[2], r);
					}
					catch(ArrayIndexOutOfBoundsException e){
						System.out.println("determine needs a source and location.");
					}
					catch(SecurityException e){
						e.printStackTrace();
					}
					catch(FileNotFoundException f){
						System.out.println(f);
					}
					catch(IOException e){
						e.printStackTrace();
					}
					break;
				case "test":
					File sauce = new File("C:\\testfile.txt");
					String test = "This string is part of a test for Java's I/O operations.";
					if(sauce.exists()){
						File ne = new File(sauce.getParent() + "\\lax.txt");
						if(sauce.renameTo(ne))
							System.out.println("Renaming finished.");
						else
							System.out.println("Renaming did not happen.");
					}
					byte buf[] = test.getBytes();

					try(FileOutputStream out = new FileOutputStream(sauce)){
						out.write(buf);
					}
					catch(IOException e){
						e.printStackTrace();
					}
					finally{
						System.out.println("The operation completed successfully!");
					}
					break;
				case "test_0.3":
					//dtool.display("now its time to test the functions of version 0.3");
					String[] types = new String[]{".mp4", ".txt", ".java"};
					ArrayList<Path> test_paths = btools.extract_all(Paths.get(""));
					for(Path a: test_paths){
						//dtool.display(a.toFile().getName() );
						//dtool.display("output: "+hasStr(a.toFile().getName(), types) );
					}
					//dtool.text_menu(reader, System.out);
					break;
				case "test_0.7.1":
					String[] prompts = {"write", "i", "write_in_i"};
					System.out.println("Enter a prompt...");
					System.out.println(has_option(prompts, reader.nextLine()) );
					break;
				case "has_parameters":
					System.out.println("Time to test 0.5 function.");
					System.out.println("Enter the word command followed by parameters.");
					System.out.println("If the word command simply appears from the following output, the function didn't detect parameters.");
					String c = "command";
					String input = reader.nextLine();
					for(String p: has_parameter(c, input))
						System.out.println(p);
					System.out.println("---");
					for(String p: has_parameter(null, input) )
						System.out.println(p);
					System.out.println("---");
					System.out.println("has for command: "+has(input, c) );
					System.out.println("contains for command: "+input.contains(c) );
					break;
				case "test_logger":
					System.out.println("Test for logger.");
					StreamHandler sh = new StreamHandler(System.out, new SimpleFormatter() );
					sh.setLevel(Level.FINE);
					btools_log.addHandler(sh);
					btools_log.setLevel(Level.ALL);
					System.out.println(has("test", "te") );
					btools_log.log(Level.FINE, "This should appear in log.");
					sh.flush();
					break;
				default:
					System.out.println("Not a valid command.");
					break;
			}
		}
		else{
			System.out.println("Enter parameters.");
		}
	}
}