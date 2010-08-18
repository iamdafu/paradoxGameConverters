package eu3tovic2.tasks;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Date;

import savefileconverter.ConversionStatus;
import eu3tovic2.ConversionState;
import eu3tovic2.vic2model.Province;
import eu3tovic2.vic2model.World;

public class SaveVic2 implements Runnable {
	private final ConversionState state;
	private final ConversionStatus status;
	
	private PrintStream stream;

	public SaveVic2(ConversionState state, ConversionStatus status) {
		this.state = state;
		this.status = status;
		
		state.outputFile = new File(state.inputFile.getPath() + ".v2"); //TODO: set in conversion class later
	}
	
	@Override
	public void run() {
		try {
			stream = new PrintStream(new FileOutputStream(state.outputFile));
		} catch (FileNotFoundException e) {
			status.printError("Couldn't open " + state.outputFile + " for writing");
			state.errorCount++;
		}
		
		writeWorld(state.worldModel);
		stream.close();
	}

	@Override
	public String toString() {
		return "Save to Vic 2 save file";
	}
	
	private void writeWorld(World world) {
		writeHeader(world);
		
		for (Province p : world.getProvinces()) {
			writeProvince(p);
		}
	}
	
	private void writeHeader(World world) {
		stream.print("date = \"");
		printDate(world.getDate());
		stream.println("\"");
		stream.println("player = \"" + world.getPlayer() + "\"");
		stream.print("start_date = \"");
		printDate(world.getDate());
		stream.println("\"");
	}
	
	@SuppressWarnings("deprecation")
	private void printDate(Date date) {
		stream.print(date.getYear());
		stream.print('.');
		stream.print(date.getMonth());
		stream.print('.');
		stream.print(date.getDate());
	}

	private void writeProvince(Province province) {
		stream.print(province.getTag());
		stream.println(" = {");
		stream.println("}");
	}
}
