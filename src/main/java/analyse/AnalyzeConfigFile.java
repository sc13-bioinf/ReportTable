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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import utilities.Pipelines;
import IO.Communicator;

import com.thoughtworks.xstream.XStream;

/**
 * This class reads the configuration file, generated by the EAGER pipeline
 * to identify the parts of the pipeline that were executed.<p>
 * afterwards only the parts that were actually executed have to be analyzed
 *
 * @author Alexander Seitz
 *
 */
public class AnalyzeConfigFile {

	// members
	private File configFile;
	private boolean analysisSuccessful = true;
	private HashMap<Pipelines, Boolean> runPipelines;

	/**
	 * Constructor, reads the serialized Communicator file and analyzes, which
	 * parts of the pipeline were executed
	 * @param configFile
	 */
	public AnalyzeConfigFile(File configFile){
		this.configFile = configFile;
		// read the serialized Communicator file
		Communicator communicator = null;
		try {
			XStream xstream = new XStream();
			InputStream in = new FileInputStream(configFile);
            communicator = (Communicator) xstream.fromXML(in);
//			objectInputStream = new ObjectInputStream(new FileInputStream(this.configFile));
//			communicator = (Communicator) objectInputStream.readObject();
		}catch (com.thoughtworks.xstream.mapper.CannotResolveClassException e) {
			this.analysisSuccessful = false;
			System.err.println("Could not resolve the configuration class");
			System.err.println(this.configFile.getAbsolutePath());
			System.err.println("Analysis will be continued without analyzing the configuration file");
		} catch (FileNotFoundException e) {
			this.analysisSuccessful = false;
			System.err.println("Configuration File not found:");
			System.err.println(this.configFile.getAbsolutePath());
			System.err.println("Analysis will be continued without analyzing the configuration file");
//		} catch (@SuppressWarnings("hiding") IOException e) {
//			this.analysisSuccessful = false;
//			System.err.println("Could not read config File:");
//			System.err.println(this.configFile.getAbsolutePath());
//			System.err.println("Analysis will be continued without analyzing the configuration file");
		} catch (Exception e){
			this.analysisSuccessful = false;
			System.err.println("An error occured while reading the config file:");
			System.err.println(this.configFile.getAbsolutePath());
			System.err.println("Analysis will be continued without analyzing the configuration file");
      //System.err.println(e.getMessage());
		}
		// if the configuration file could be read, analyze it.
		this.runPipelines = new HashMap<Pipelines, Boolean>();
		if(analysisSuccessful){
			Boolean fastqc = communicator.isRun_fastqc();
			if(null != fastqc && communicator.isRun_fastqc()){
				this.runPipelines.put(Pipelines.FastQC, true);
			}else{
				this.runPipelines.put(Pipelines.FastQC, false);
			}
			if(communicator.isRun_clipandmerge()){
				this.runPipelines.put(Pipelines.ClipAndMerge, true);
			}else{
				this.runPipelines.put(Pipelines.ClipAndMerge, false);
			}
			if(communicator.isRun_qualityfilter()){
				this.runPipelines.put(Pipelines.QualityTrimming, true);
			}else{
				this.runPipelines.put(Pipelines.QualityTrimming, false);
			}
      if(communicator.getMapper_to_use().equals("CircularMapper")){
        this.runPipelines.put(Pipelines.CircularMapper, true);
      }else{
        this.runPipelines.put(Pipelines.CircularMapper, false);
      }
			if(communicator.isRun_mapping()){
				this.runPipelines.put(Pipelines.Mapping, true);
				this.runPipelines.put(Pipelines.Samtools, true);

			}else{
				this.runPipelines.put(Pipelines.Mapping, false);
				this.runPipelines.put(Pipelines.Samtools, false);
			}
			if(communicator.isRmdup_run()){
				this.runPipelines.put(Pipelines.DeDup, true);
			}else{
				this.runPipelines.put(Pipelines.DeDup, false);
			}
			if(communicator.isSchmutzi_run()){
				this.runPipelines.put(Pipelines.Schmutzi, true);
			}else{
				this.runPipelines.put(Pipelines.Schmutzi, false);
			}
			if(communicator.isRun_coveragecalc()){
				this.runPipelines.put(Pipelines.QualiMap, true);
			}else{
				this.runPipelines.put(Pipelines.QualiMap, false);
			}
			if(communicator.isRun_mapdamage()){
				this.runPipelines.put(Pipelines.MapDamage, true);
			}else{
				this.runPipelines.put(Pipelines.MapDamage, false);
			}
			if(communicator.isRun_complexityestimation()){
				this.runPipelines.put(Pipelines.Preseq, true);
			}else{
				this.runPipelines.put(Pipelines.Preseq, false);
			}
			if(communicator.isRun_gatksnpcalling()){
				this.runPipelines.put(Pipelines.GATKBasics, true);
				this.runPipelines.put(Pipelines.GATKGenotyper, true);
			}else{
				this.runPipelines.put(Pipelines.GATKBasics, false);
				this.runPipelines.put(Pipelines.GATKGenotyper, false);
			}
			if(communicator.isRun_gatksnpfiltering()){
				this.runPipelines.put(Pipelines.GATKVariantFilter, true);
			}else{
				this.runPipelines.put(Pipelines.GATKVariantFilter, false);
			}
			if(communicator.isRun_vcf2draft()){
				this.runPipelines.put(Pipelines.VCF2Genome, true);
			}else{
				this.runPipelines.put(Pipelines.VCF2Genome, false);
			}
		}
	}

	/**
	 * return if the analysis of the configuration file was successful
	 * @return if the analysis of the configuration file was successful
	 */
	public boolean isAnalysisSuccessful() {
		return analysisSuccessful;
	}

	/**
	 * return a HashMap that shows which of the steps in the pipeline were run (according to the configuration file
	 * @return which steps of the pipeline were run
	 */
	public HashMap<Pipelines, Boolean> getRunPipelines() {
		return runPipelines;
	}

}
