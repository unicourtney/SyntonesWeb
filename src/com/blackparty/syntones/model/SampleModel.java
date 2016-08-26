package com.blackparty.syntones.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

@Entity
@Table(name = "SAMPLE_MODEL_TBL")
public class SampleModel {

	@Id
	@TableGenerator(name = "table_gen", table = "sequence_table", pkColumnName = "seq_name", valueColumnName = "seq_count", pkColumnValue = "sample_model_seq")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "table_gen")
	private long columnId;

	@Column(name = "SAMPLE_STRING")
	private String sampleString;

	public SampleModel() {
	}

	public SampleModel(String sampleString) {
		this.sampleString = sampleString;
	}

	public long getColumnId() {
		return columnId;
	}

	public String getSampleString() {
		return sampleString;
	}

	public void setSampleString(String sampleString) {
		this.sampleString = sampleString;
	}

}
