package processor;

import java.util.Map;

public enum ThresholdColorGetter {
	COMMENTS_METRIC (3, 7),
	CODESIZE_METRIC (2, 5);
	private int [] WARNING_LIMITS;
	private static final String[] colors = {"Green", "Blue", "Purple", "Yellow", "Orange","Red"};
	private ThresholdColorGetter(int... thresholds){
		WARNING_LIMITS = thresholds;
	}
	public String getColor(Integer warnings) 
	{
		if (warnings == null) warnings = 0;
		int colorPos = search(WARNING_LIMITS, warnings);
		int pos = colorPos*(colors.length-1)/(WARNING_LIMITS.length);
		return colors[pos];
	}
	private int search(int[] array, int key) {
		for(int i=0; i<array.length; i++){
			if(array[i] > key) return i;
		}
		return array.length;
	}
}