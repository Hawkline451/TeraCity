package org.terasology.codecity.world.map;

import java.util.Arrays;

import org.terasology.codecity.world.structure.scale.CodeScaleManager;
import org.terasology.registry.CoreRegistry;
import org.terasology.world.block.Block;
import org.terasology.world.block.BlockManager;

/*
 * Only works with HalfLinearCodeScale
 */
public class IndexCodeBlockFactory {
	
/**
 * Returns the block associated with a binary representation
 * @param block      Binary representation of the code chunk
 * @return           Block representing that chunk
 */
  public static Block generate() {

    return CoreRegistry.get(BlockManager.class).getBlock("core:lava");

  }
  public static int[][] recalcBinary(DrawableCode code,int row,int column) {
	  int[][] fullRep = code.getFullRep();
	  if (! code.useTexture()){
		  return null;
	  }
	  
	int[][] binaryRepr = transformCodeLine(fullRep);
    int[][] temp = new int[2][2];
    //TODO FIX VERTICAL SCALE GET
    try {
      temp[0][0] = binaryRepr[row * 2][column * 2];
      temp[0][1] = binaryRepr[row * 2][column * 2 + 1];
      temp[1][0] = binaryRepr[row * 2 + 1][column * 2];
      temp[1][1] = binaryRepr[row * 2 + 1][column * 2 + 1];
    } catch (IndexOutOfBoundsException e) {
      System.err.println("Row col: " +row+" "+column+" "+e.getMessage());
      return null;
    }
    return temp;
  }
  
	 private static int[][] transformCodeLine(int[][] array){
		 int ratio1Cols = array[0].length;
		  CodeScaleManager man = CoreRegistry.get(CodeScaleManager.class);
		  int maxBlockLength = man.getHorizontalScale().getScaledSize(ratio1Cols);
	
		 int[][] result = new int[array.length][maxBlockLength];
		  
		 for (int rowIndex=0;rowIndex< array.length ; rowIndex++){
			 int[] row = array[rowIndex];
			  int[] resultRow = new int[2*maxBlockLength];
			  
			  float step =  ratio1Cols/(2*maxBlockLength);
			  if (step > 1){ //Step entero muchos caracteres por celda
				  int stepInt = (int)step;
				  for (int i=0;i<2*maxBlockLength;i++){
					  int[] slice = Arrays.copyOfRange(row, stepInt*i, stepInt*(i+1));
					  
					  if (Arrays.toString(slice).contains("1")){
						  resultRow[i] = 1;
					  }
					  else{
						  resultRow[i] = 0;
					  }
				  }
			  }
			  else{ //Caso step flotante muchas celdas por caracter
				  int stepInt = (2*maxBlockLength)/ratio1Cols;
				  for (int i=0;i<ratio1Cols;i++){					  
					  for (int j=0;j<stepInt;j++){
						  if (row[i] == 1){
							  resultRow[i*stepInt+j] = 1;
						  }
						  else{
							  resultRow[i*stepInt+j] = 0;
						  }
					  }
				  }
			  }

			  result[rowIndex] = resultRow;
		 }
		  return result;
	 }
  
  @Deprecated
	public static Block generate(int[] lineLength, int blockNum) {
	  return CoreRegistry.get(BlockManager.class).getBlock("core:lava");
	}
}
