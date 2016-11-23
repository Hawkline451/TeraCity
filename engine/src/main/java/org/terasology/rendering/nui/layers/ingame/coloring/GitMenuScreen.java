package org.terasology.rendering.nui.layers.ingame.coloring;

import java.util.ArrayList;
import java.util.Calendar;

import org.terasology.config.Config;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.logic.console.Console;
import org.terasology.logic.console.commandSystem.ConsoleCommand;
import org.terasology.logic.console.commandSystem.exceptions.CommandExecutionException;
import org.terasology.naming.Name;
import org.terasology.registry.In;
import org.terasology.rendering.nui.CoreScreenLayer;
import org.terasology.rendering.nui.UIWidget;
import org.terasology.rendering.nui.WidgetUtil;
import org.terasology.rendering.nui.databinding.Binding;
import org.terasology.rendering.nui.widgets.ActivateEventListener;
import org.terasology.rendering.nui.widgets.UIDropdown;
import org.terasology.rendering.nui.widgets.UILabel;
import org.terasology.rendering.nui.widgets.UIText;

import com.google.common.collect.Lists;

/**
 * Class that manages the UI for Git Menu Screen, from ingame Coloring menu.
 * This works with gitMenuScreen.ui, found in:
 * engine/src/main/resources/assets/ui/ingame/coloring/
 *
 */
public class GitMenuScreen extends CoreScreenLayer{

	    @In
	    private Config config;
	    
	    @In
	    private Console console;

	    @Override
	    @SuppressWarnings("unchecked")
	    public void initialise() {
	        

	    	final UIDropdown<FaceToPaint> faceToPaint = find("faceToPaint", UIDropdown.class);
	        if (faceToPaint != null) {
	            faceToPaint.setOptions(Lists.newArrayList(FaceToPaint.ALL, FaceToPaint.NORTH, FaceToPaint.EAST, FaceToPaint.WEST, FaceToPaint.SOUTH));
	        }
	        
	        final UIDropdown<ColorScale> colorScale = find("colorScale", UIDropdown.class);
	        if (colorScale != null) {
	        	colorScale.setOptions(Lists.newArrayList(ColorScale.RAINBOW,ColorScale.RED, ColorScale.ORANGE,ColorScale.YELLOW,ColorScale.GREEN,ColorScale.BLUE));
	        }
	        
	        final UIText dateYear = find("dateYear", UIText.class);
	        if (dateYear != null) {
	        	dateYear.bindText(new Binding<String>() {
	        		String year;
	        		
					@Override
					public String get() {
						if (year == null) {
							set("yyyy");
						}
						return year;
					}

					@Override
					public void set(String value) {
						year = value;
					}
	        		
	        	});
	        }
	        
	        final UIText dateMonth = find("dateMonth", UIText.class);
	        if (dateMonth != null) {
	        	dateMonth.bindText(new Binding<String>() {
	        		String month;
	        		
					@Override
					public String get() {
						if (month == null) {
							set("mm");
						}
						return month;
					}

					@Override
					public void set(String value) {
						month = value;
					}
	        		
	        	});
	        }
	        
	        final UIText dateDay = find("dateDay", UIText.class);
	        if (dateDay != null) {
	        	dateDay.bindText(new Binding<String>() {
	        		String day;
	        		
					@Override
					public String get() {
						if (day == null) {
							set("dd");
						}
						return day;
					}

					@Override
					public void set(String value) {
						day = value;
					}
	        		
	        	});
	        }
	        
	        final UIText projectURL = find("projectURL", UIText.class);
	        final UIText projectName = find("projectName", UIText.class);
	        
	        // displays info to the user: warnings, errors, ...
	        final UILabel infoField = find("infoField", UILabel.class);
	        
	        WidgetUtil.trySubscribe(this, "bugs", new ActivateEventListener() {
	            @Override
	            public void onActivated(UIWidget widget) {
	            	FaceToPaint face = faceToPaint.getSelection();
	            	ColorScale color = colorScale.getSelection();
	            	String year = dateYear.getText();
	            	String month = dateMonth.getText();
	            	String day = dateDay.getText();
	            	String url = projectURL.getText();
	            	String name = projectName.getText();
	            	if (checkDateFormat(day, month, year, infoField)) {
	            		day = addZeroToNumber(day);
	            		month = addZeroToNumber(month);
	            		String date = year + "/" + month + "/" + day;
		            	executeCommand("bugs", face, color, url, name, date);
	            	}
	            }
	        });
	        WidgetUtil.trySubscribe(this, "versions", new ActivateEventListener() {
	            @Override
	            public void onActivated(UIWidget widget) {
	            	FaceToPaint face = faceToPaint.getSelection();
	            	ColorScale color = colorScale.getSelection();
	            	String year = dateYear.getText();
	            	String month = dateMonth.getText();
	            	String day = dateDay.getText();
	            	String url = projectURL.getText();
	            	String name = projectName.getText();
	            	if (checkDateFormat(day, month, year, infoField)) {
	            		day = addZeroToNumber(day);
	            		month = addZeroToNumber(month);
	            		String date = year + "/" + month + "/" + day;
		            	executeCommand("versions", face, color, url, name, date);
	            	}
	            }
	        });
	       
	        
	    
	        WidgetUtil.trySubscribe(this, "close", new ActivateEventListener() {
	            @Override
	            public void onActivated(UIWidget button) {
	            	infoField.setText("");
	            	config.save();
	            	getManager().popScreen();
	            }
	        });
	    }
	    
	    /**
	     * Checks that the day given by the user has a correct format. That means a day number
	     * must not be negative, nor 0, nor exceeding month's length, even considering if the given
	     * month is February and year is a leap year.
	     * 
	     * @param day		The day selected by the user.
	     * @param month		The month selected by the user.
	     * @param year		The year selected by the user.
	     * 
	     * @return			True if it's a valid day. False otherwise.
	     */
	    private boolean checkDateFormat(String day, String month, String year, UILabel infoField) {
	    	ArrayList<String> monthsWith30Days = Lists.newArrayList("04", "06", "09", "11", "4", "6", "9");
	    	Calendar c = Calendar.getInstance();
	    	int numberOfDay, lastMonthDay, yearNumber, numberOfMonth;
	    	try {
		    	numberOfDay = Integer.parseInt(day);
		    	yearNumber = Integer.parseInt(year);
		    	numberOfMonth = Integer.parseInt(month);
	    	} catch(NumberFormatException e) {
	    		infoField.setText("Warning: Date must be written in numbers!");
	    		return false;
	    	}
	    	if (numberOfMonth < 1 || numberOfMonth > 12) {
	    		infoField.setText("Warning: Month must be a number between 01 and 12!");
	    		return false;
	    	} else if (yearNumber < 2008) {
	    		infoField.setText("Warning: Year must be a number between 2008! (There was no GitHub before)");
	    		return false;
	    	} else if (numberOfDay <= 0) {
	    		infoField.setText("Warning: Day must be at least 01!");
	    		return false;
	    	} else if (numberOfMonth == 2) {
	    		if (yearNumber % 4 != 0) {
	    			lastMonthDay = 28;
	    		} else if (yearNumber % 100 != 0) {
	    			lastMonthDay = 29;
	    		} else if (yearNumber % 400 != 0) {
	    			lastMonthDay = 28;
	    		} else {
	    			lastMonthDay = 29;
	    		}
	    	} else if (monthsWith30Days.contains(month)) {
	    		lastMonthDay = 30;
	    	} else {
	    		lastMonthDay = 31;
	    	}
	    	if (numberOfDay > lastMonthDay) {
		    	infoField.setText("Warning: That month didn't have that much days!");
	    		return false;
	    	}
	    	if (yearNumber == c.get(Calendar.YEAR)) {
	    		if (numberOfMonth == c.get(Calendar.MONTH) + 1) {
	    			if (numberOfDay == c.get(Calendar.DAY_OF_MONTH)) {
	    				//TODO will the new command work if the given date is "today"?
	    			} else if (numberOfDay > c.get(Calendar.DAY_OF_MONTH)) {
	    				infoField.setText("Warning: You can't check future commits!");
	    				return false;
	    			}
	    		} else if (numberOfMonth > c.get(Calendar.MONTH) + 1) {
    				infoField.setText("Warning: You can't check future commits!");
    				return false;
	    		}
	    	} else if (yearNumber > c.get(Calendar.YEAR)) {
				infoField.setText("Warning: You can't check future commits!");
				return false;
	    	}
	    	
	    	infoField.setText("");
	    	return true;
	    }
	    
	    /**
	     * Formalizes date format for day/month if they're lesser than 10,
	     * by adding 0 at the start.
	     * 
	     * @param number	day/month to be formalized.
	     * 
	     * @return			formalized day/month
	     */
	    private String addZeroToNumber(String number) {
	    	int n;
	    	if (number.startsWith("0")) {
	    		return number;
	    	}
	    	try {
	    		n = Integer.parseInt(number);
		    	if (n < 10) {
		    		number = "0" + number;
		    	}
	    	} catch(NumberFormatException e) {
	    		// If this method is used, then it'll never throw that exception.
	    	}
	    	return number;
	    }
	    
	    private void executeCommand(String metric, FaceToPaint face, ColorScale color,
	    		String url, String name, String date) {
	    	
	    	// manage invalid face selections
	    	final UILabel infoField = find("infoField", UILabel.class);
	    	if (face == null || color == null) {
        		infoField.setText("Warning: please choose a face and a color to paint!");
        		return;
        	}
        	infoField.setText("");
	    	
	    	ConsoleCommand ca = console.getCommand(new Name("paintWithGit"));
	    	//ConsoleCommand ca = console.getCommand(new Name("gitCommit"));
	    	//TODO change command name according to new git command
	    	ArrayList<String> params = new ArrayList<String>();
	    	if (name.equals("")) {
	    		name = "WorldCodecity";
	    	}
	    	//String url = "";
	    	String metricString = metric;
			params.add(metricString);
	    	params.add(url);
	    	params.add(name);
	    	params.add(face.toString());
	    	params.add(color.toString());
	    	//params.add(date);
	    	//TODO add parameter "date" according to new command's parameters
	    	EntityRef e = null;
	    	try {
				ca.execute(params, e);
			} catch (CommandExecutionException e1) {
			}
	    }

	    @Override
	    public boolean isLowerLayerVisible() {
	        return false;
	    }
}
