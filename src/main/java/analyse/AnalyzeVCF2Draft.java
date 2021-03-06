/*
 * Copyright (c) 2016 ReportTable Alexander Seitz
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

/**
 * 
 */
package analyse;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import utilities.OutputStrings;
import utilities.Pipelines;

/**
 * Class for the analsyis of the results generated by the
 * better remove duplicates step of the pipeline.
 * 
 * @author Alexander Seitz
 *
 */
public class AnalyzeVCF2Draft extends AbstractAnalyze{
	
	// the number of SNPs that were called
	private String numSNPs = OutputStrings.notFound;

	/**
	 * Constructor, the given Folder should point to the root directory of
	 * the sample, containing the folders generated by the pipeline.
	 * @param currentFolder	the root folder of the sample
	 */
	public AnalyzeVCF2Draft(File currentFolder) {
		// init the variables
		super(currentFolder);
		// get the name of the Subdirectory containing the needed File
//		File dataDir = new File(this.sampleFolder.getAbsolutePath() + "/" + Pipelines.VCF2Genome.toString());
		File dataDir = this.getCurrFolder(Pipelines.VCF2Genome);
		// only continue, if this directory exists
//		if(dataDir.exists()){
		if(dataDir != null){
			// iterate through all files in the folder, until the desired File is found
			// parse this file
			String[] names = dataDir.list();
			for(String name:names){
				File currFile = new File(dataDir+"/"+name);
				if(currFile.isFile()){
					if(currFile.getName().endsWith(".stats")){
						parseFile(currFile);
					}
				}
			}
		}
	}

	// parse the given file for the needed information
	private void parseFile(File currFile) {
		String lastLine = tail(currFile);
		String[] splitted = lastLine.split("\t");
		if(splitted.length>1){
			if(lastLine != null && !lastLine.contains("SNP Calls")){
				this.numSNPs = splitted[1].trim();
			}
//			if(!"SNP Calls".equals(splitted[1].trim())){
//				this.numSNPs = splitted[1].trim();
//			}
		}
		// old format
//		try {
//			@SuppressWarnings("resource")
//			BufferedReader br = new BufferedReader(new FileReader(currFile));
//			String currLine = "";
//			while((currLine = br.readLine()) != null){
//				if(currLine.contains("variant positions have been called")){
//					String[] lineSplitted = currLine.split(" ");
//					if(lineSplitted.length > 1){
//						this.numSNPs = lineSplitted[0].trim();
//					}
//				}
//			}
//		} catch (IOException e) {
//		}
	}
	
	private String tail( File file ) {
	    RandomAccessFile fileHandler = null;
	    try {
	        fileHandler = new RandomAccessFile( file, "r" );
	        long fileLength = fileHandler.length() - 1;
	        StringBuilder sb = new StringBuilder();

	        for(long filePointer = fileLength; filePointer != -1; filePointer--){
	            fileHandler.seek( filePointer );
	            int readByte = fileHandler.readByte();

	            if( readByte == 0xA ) {
	                if( filePointer == fileLength ) {
	                    continue;
	                }
	                break;

	            } else if( readByte == 0xD ) {
	                if( filePointer == fileLength - 1 ) {
	                    continue;
	                }
	                break;
	            }

	            sb.append( ( char ) readByte );
	        }

	        String lastLine = sb.reverse().toString();
	        return lastLine;
	    } catch( java.io.FileNotFoundException e ) {
	        e.printStackTrace();
	        return null;
	    } catch( java.io.IOException e ) {
	        e.printStackTrace();
	        return null;
	    } finally {
	        if (fileHandler != null )
	            try {
	                fileHandler.close();
	            } catch (IOException e) {
	                /* ignore */
	            }
	    }
	}

	/**
	 * Getter method for the field containing the number of called SNPs
	 * @return the number of called SNPs
	 */
	public String getNumSNPs() {
		return numSNPs;
	}
}
