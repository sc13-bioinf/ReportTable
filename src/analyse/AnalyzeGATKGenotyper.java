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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import utilities.OutputStrings;
import utilities.Pipelines;

/**
 * Class for the analsyis of the results generated by the
 * GATK Genotyper step of the pipeline
 * 
 * @author Alexander Seitz
 *
 */
public class AnalyzeGATKGenotyper extends AbstractAnalyze {
	
	// member variables
	private String positionsFoundByGenotyper = OutputStrings.notFound;
	private String checkedPositionsAgainstList = OutputStrings.notFound;
	private String snpPercentage = OutputStrings.notFound;
	private String numHQPositions = OutputStrings.notFound;
	private String numLQPositions = OutputStrings.notFound;
	private String coverageHQ = OutputStrings.notFound;
	private String coverageLQ = OutputStrings.notFound;
	private String coverageVerified = OutputStrings.notFound;

	/**
	 * Constructor, the given Folder should point to the root directory of
	 * the sample, containing the folders generated by the pipeline
	 * @param sampleFolder
	 */
	public AnalyzeGATKGenotyper(File sampleFolder) {
		super(sampleFolder);
		this.sampleFolder = sampleFolder;
//		File dataDir = new File(this.sampleFolder.getAbsolutePath() + "/" +Pipelines.GATKGenotyper.toString());
		File dataDir = this.getCurrFolder(Pipelines.GATKGenotyper);
//		if(dataDir.exists()){
		if(dataDir != null){
			String[] names = dataDir.list();
			for(String name:names){
				File currFile = new File(dataDir.getAbsolutePath()+"/"+name);
				if(currFile.isFile() && currFile.getName().endsWith("snpcc")){
					parseFile(currFile);
				}
			}
		}
	}
	

	// parse the given file
	private String parseFile(File currFile) {
		String result = OutputStrings.notFound;
		try {
			@SuppressWarnings("resource")
			BufferedReader br = new BufferedReader(new FileReader(currFile));
			String currLine = "";
			while((currLine = br.readLine()) != null){
				if(currLine.contains("Total number of found positions by the Genotyper")){
					String[] splitted = currLine.split(":");
					this.positionsFoundByGenotyper = splitted[splitted.length-1].trim();
					continue;
				}
				if(currLine.contains("Total number of cross checked positions (against list)")){
					String[] splitted = currLine.split(":");
					this.checkedPositionsAgainstList = splitted[splitted.length-1].trim();
					continue;
				}
				if(currLine.contains("Percentage of SNP Positions covered")){
					String[] splitted = currLine.split(":");
					this.snpPercentage = splitted[splitted.length-1].trim();
					this.snpPercentage = this.snpPercentage.replace(",", ".");
					this.snpPercentage += OutputStrings.perCent;
					continue;
				}
				if(currLine.contains("Total number of HQ Positions (filtered against List)")){
					String[] splitted = currLine.split(":");
					this.numHQPositions = splitted[splitted.length-1].trim();
					continue;
				}
				if(currLine.contains("Total number of LQ Positions (filtered against List)")){
					String[] splitted = currLine.split(":");
					this.numLQPositions = splitted[splitted.length-1].trim();
					continue;
				}
				if(currLine.contains("Coverage on HQ SNP sites")){
					String[] splitted = currLine.split(":");
					this.coverageHQ = splitted[splitted.length-1].trim();
					this.coverageHQ = this.coverageHQ.replace(",", ".");
					this.coverageHQ += OutputStrings.perCent;
					continue;
				}
				if(currLine.contains("Coverage on LQ SNP sites")){
					String[] splitted = currLine.split(":");
					this.coverageLQ = splitted[splitted.length-1].trim();
					this.coverageLQ = this.coverageLQ.replace(",", ".");
					this.coverageLQ += OutputStrings.perCent;
					continue;
				}
				if(currLine.contains("AVG Coverage on Verified Sites (against list)")){
					String[] splitted = currLine.split(":");
					this.coverageVerified = splitted[splitted.length-1].trim();
					this.coverageVerified = this.coverageVerified.replace(",", ".");
					this.coverageVerified += OutputStrings.perCent;
					continue;
				}
			}
		} catch (IOException e) {
		}
		return result;
	}



	/**
	 * Getter method for the field containing the number of positions that were
	 * found by the genotyper
	 * @return the number of positions found by the Genotyper
	 */
	public String getPositionsFoundByGenotyper() {
		return positionsFoundByGenotyper;
	}



	/**
	 * Getter method for the field containing the number of positions that were checked
	 * against the reference list
	 * @return the number of positions checked  against the list
	 */
	public String getCheckedPositionsAgainstList() {
		return checkedPositionsAgainstList;
	}



	/**
	 * Getter method for the field containing the percentage of the covered SNPs
	 * @return the SNP Percentage
	 */
	public String getSnpPercentage() {
		return snpPercentage;
	}



	/**
	 * GetterMethod for the field containing the number of HQ Positions that were covered
	 * @return the number of covered HQ Positions
	 */
	public String getNumHQPositions() {
		return numHQPositions;
	}



	/**
	 * GetterMethod for the number of LQ Positions that were covered
	 * @return the number of covered LQ Positions
	 */
	public String getNumLQPositions() {
		return numLQPositions;
	}



	/**
	 * Getter method for the field containing the coverage on HQ Sites
	 * @return the coverage of the HQ sites
	 */
	public String getCoverageHQ() {
		return coverageHQ;
	}



	/**
	 * Getter method for the field containing the coverage on LQ Sites
	 * @return the coverage of the LQ sites
	 */
	public String getCoverageLQ() {
		return coverageLQ;
	}



	/**
	 * Getter method for the field containing the coverage on verified Sites
	 * @return the coverage of the verified sites
	 */
	public String getCoverageVerified() {
		return coverageVerified;
	}

}
