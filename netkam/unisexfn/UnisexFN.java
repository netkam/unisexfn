package netkam.unisexfn;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.regex.Matcher;

/** 
 * @author netkam
 * @version 0.1
 */

public class UnisexFN {

	private UnisexFN() {}
	
	
	public static Optional<String> extractName(String arg1) {
		
		Optional<Integer> lastFileSeparator = UnisexFN.detectLastFileSeparator(arg1);
		
		String name = arg1.substring( (lastFileSeparator.orElse(-1) + 1) );
		
		return name.isEmpty()?
				Optional.empty()
				:
				Optional.ofNullable(name);
	}
	
	public static Optional<String> extractBaseName(String arg1) {
		
		Optional<Integer> lastFileSeparator = UnisexFN.detectLastFileSeparator(arg1);
		Optional<Integer> fileExtensionSeparator = UnisexFN.detectFileExtensionSeparator(arg1);
		
		String baseName = fileExtensionSeparator.isPresent()?
				arg1.substring(
							lastFileSeparator.orElse(-1) + 1,
							fileExtensionSeparator.get()
						)
				:
				arg1.substring( (lastFileSeparator.orElse(-1) + 1) );
		
		return baseName.isEmpty()?
				Optional.empty()
				:
				Optional.ofNullable(baseName);
	}
	
	public static Optional<String> extractExtension(String arg1) {
		
		Optional<Integer> fileExtensionSeparator = UnisexFN.detectFileExtensionSeparator(arg1);
	
		return fileExtensionSeparator.isPresent()?
				Optional.ofNullable(arg1.substring( (fileExtensionSeparator.get() + 1) ))
				:
				Optional.empty();
	}
	
	public static Optional<String> extractIfIsExtension(String arg1, String...arg2) {
		
		return UnisexFN.extractIfIsExtension(arg1, Arrays.asList(arg2));
	}
	
	public static Optional<String> extractIfIsExtension(String arg1, Collection<String> arg2) {
		
		Optional<Integer> fileExtensionSeparator = UnisexFN.detectFileExtensionSeparator(arg1);
		
		if(fileExtensionSeparator.isPresent())
			if(arg2.stream().anyMatch( x -> arg1.matches("^.*\\." + x + "$") ))
				return Optional.ofNullable(arg1.substring( (fileExtensionSeparator.orElse(-1) + 1) ));
		
		return Optional.empty();
	}
	
	public static String transform(String arg1) {
		
		return UnisexFN.transform(arg1, OSTypeDetector.getCurrentOSType());
	}
	
	public static String transform(String arg1, OSType arg2) {
		
		return UnisexFN.replaceFileSeparators(arg1, arg2.fileSeparator);
	}
	
	public static String removeExtension(String arg1) {
		
		Optional<Integer> fileExtensionSeparator = UnisexFN.detectFileExtensionSeparator(arg1);
		
		return fileExtensionSeparator.isPresent()?
				arg1.substring(0, fileExtensionSeparator.get())
				:
				arg1;
	}
	
	public static boolean hasExtension(String arg1) {
		
		return UnisexFN.detectFileExtensionSeparator(arg1).isPresent();
	}
	
	public static boolean isExtension(String arg1, String...arg2) {
		
		return UnisexFN.isExtension(arg1, Arrays.asList(arg2));
	}
	
	public static boolean isExtension(String arg1, Collection<String> arg2) {
		
		return UnisexFN.detectFileExtensionSeparator(arg1).isPresent()?
				arg2.stream().anyMatch(  x -> arg1.matches("^.*\\." + x + "$") )
				:
				false;
	}
	
	
	private static String replaceFileSeparators(String arg1, String arg2) {
		
		return arg1.replaceAll(
				Matcher.quoteReplacement(
						new StringBuilder()
							.append('[')
							.append(OSType.UNIX_LIKE.fileSeparator)
							.append(OSType.WINDOWS.fileSeparator)
							.append(']')
							.toString()
						),
				Matcher.quoteReplacement(arg2)
				);
	}
	
	private static Optional<Integer> detectLastFileSeparator(String arg1) {
		
		Integer lastFileSeparator = Integer.max(
					arg1.lastIndexOf(OSType.UNIX_LIKE.fileSeparator),
					arg1.lastIndexOf(OSType.WINDOWS.fileSeparator)
				);
		
		return lastFileSeparator == -1?
				Optional.empty()
				:
				Optional.ofNullable(lastFileSeparator);
	}
	
	private static Optional<Integer> detectFileExtensionSeparator(String arg1) {
		
		Integer lastDot = arg1.lastIndexOf(UnisexFN.FILE_EXTENSION_SEPARATOR_CHAR);
		Integer lastFileSeparator = UnisexFN.detectLastFileSeparator(arg1).orElse(-1);
		
		if(
				lastDot == -1
					||
				lastDot < lastFileSeparator
					||
				lastDot == (lastFileSeparator + 1)
					||
				lastDot == (arg1.length() - 1)
		) return Optional.empty();
		
		return Optional.ofNullable(lastDot);
	}
	
	
	private static final char FILE_EXTENSION_SEPARATOR_CHAR = '.';
	
}
