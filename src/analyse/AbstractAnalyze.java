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

import utilities.Pipelines;

/**
 * 
 * Abstract superclass for the analysis of the different parts of
 * the pipeline.<p>
 * 
 * @author Alexander Seitz
 *
 */
public abstract class AbstractAnalyze {
	
	protected File sampleFolder;

	/**
	 * Constructor, the given Folder should point to the root directory of
	 * the sample, containing the folders generated by the pipeline.
	 * @param sampleFolder	the root folder of the sample
	 */
	public AbstractAnalyze(File sampleFolder) {
		this.sampleFolder = sampleFolder;
	}
	
	protected File getCurrFolder(Pipelines p){
		for(String tmpDir: this.sampleFolder.list()){
			File currFile = new File(this.sampleFolder + "/" + tmpDir);
			if(currFile.isDirectory() && tmpDir.startsWith(p.toString())){
				File dataDir = new File(this.sampleFolder + "/" + tmpDir);
				return dataDir;
			}
		}
		return null;
	}

}