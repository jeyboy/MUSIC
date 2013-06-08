package outag.formats.real.utils;

import java.io.DataInputStream;
import java.util.Vector;

public class LogicalStreamInfo {
//	LogicalStream
//	{
//	  ULONG32 size;
//	  UINT16       object_version;
//
//	  if (object_version == 0)
//	  {
//	    UINT16  num_physical_streams;
//	    UINT16     physical_stream_numbers[num_physical_streams];
//	    ULONG32   data_offsets[num_physical_streams];
//	    UINT16       num_rules;
//	    UINT16        rule_to_physical_stream_number_map[num_rules];
//	    UINT16    num_properties;
//	    NameValueProperty        properties[num_properties];
//	  }
//	};
	
	public Vector<Short> ruleToPhysicalStreamNumber;
	public Vector<Short> phisicalStreamNumbers;
	public Vector<Integer> dataOffsets;
	public Vector<NameValueProperty> properties;
	
	public LogicalStreamInfo(DataInputStream f) throws Exception {
		int size = f.readInt();
		short obj_version = f.readShort();
		
		short phisicalStreamCount = f.readShort();
		phisicalStreamNumbers = new Vector<Short>(phisicalStreamCount);
		for(int loop = 0; loop < phisicalStreamCount; loop++)
			phisicalStreamNumbers.add(f.readShort());
		
		dataOffsets = new Vector<Integer>(phisicalStreamCount);
		for(int loop = 0; loop < phisicalStreamCount; loop++)
			dataOffsets.add(f.readInt());
		
		short rulesCount = f.readShort();
		ruleToPhysicalStreamNumber = new Vector<Short>(rulesCount);
		for(int loop = 0; loop < rulesCount; loop++)
			ruleToPhysicalStreamNumber.add(f.readShort());		
		
		short propertiesCount = f.readShort();
		properties = new Vector<NameValueProperty>(propertiesCount);
		for(int loop = 0; loop < propertiesCount; loop++)
			properties.add(new NameValueProperty(f));		
	}
}