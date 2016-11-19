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
	        
	        final UIDropdown<String> dateYear = find("dateYear", UIDropdown.class);
	        if (dateYear != null) {
	        	dateYear.setOptions(generateYearList());
	        	dateYear.bindSelection(new Binding<String>() {
	        		String year;
	        		
					@Override
					public String get() {
						if (year != null) {
							return year;
						} else {
							set("2008");
							return year;
						}
					}

					@Override
					public void set(String value) {
						year = value;
					}
	        		
	        	});
	        }
	        
	        final UIDropdown<String> dateMonth = find("dateMonth", UIDropdown.class);
	        if (dateMonth != null) {
	        	dateMonth.setOptions(Lists.newArrayList("January", "February", "March", "April", "May", "June", "Jule", "August", "September", "October", "November", "December"));;
	        	dateMonth.bindSelection(new Binding<String>() {
	        		String month;
	        		
					@Override
					public String get() {
						if (month != null) {
							return month;
						} else {
							set("January");
							return month;
						}
					}

					@Override
					public void set(String value) {
						month = value;
					}
	        		
	        	});
	        }
	        
	        final UIDropdown<String> dateDay = find("dateDay", UIDropdown.class);
	        if (dateDay != null) {
	        	dateDay.setOptions(generateDayList());
	        	dateDay.bindSelection(new Binding<String>() {
	        		String day;
	        		
					@Override
					public String get() {
						if (day != null) {
							return day;
						} else {
							set("01");
							return day;
						}
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
	            	String year = dateYear.getSelection();
	            	String month = dateMonth.getSelection();
	            	String day = dateDay.getSelection();
	            	String url = projectURL.getText();
	            	String name = projectName.getText();
	            	if (checkDayFormat(day, month, year)) {
		            	executeCommand("bugs", face, color, url, name);
	            	} else {
	            		infoField.setText("The day field is wrongly filled.\n"
	            				+ "Please, write a number from 1 to 31 according to selected month's length");
	            	}
	            }
	        });
	        WidgetUtil.trySubscribe(this, "versions", new ActivateEventListener() {
	            @Override
	            public void onActivated(UIWidget widget) {
	            	FaceToPaint face = faceToPaint.getSelection();
	            	ColorScale color = colorScale.getSelection();
	            	String year = dateYear.getSelection();
	            	String month = dateMonth.getSelection();
	            	String day = dateDay.getSelection();
	            	String url = projectURL.getText();
	            	String name = projectName.getText();
	            	if (checkDayFormat(day, month, year)) {
		            	executeCommand("bugs", face, color, url, name);
	            	} else {
	            		infoField.setText("The day field is wrongly filled.\n"
	            				+ "Please, write a number from 1 to 31 according to selected month's length");
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
	     * Generates a list with all years from the current one to 2008
	     * (since GitHub was launched on April 10th of 2008).
	     * 
	     * @return An ArrayList containing all years from current to 2008.
	     */
	    private ArrayList<String> generateYearList() {
	    	ArrayList<String> years = new ArrayList<String>();
	    	int currentYear = Calendar.getInstance().get(Calendar.YEAR);
	    	for(int i = currentYear; i >= 2008; i--){
	    		years.add(Integer.toString(i));
	    	}
	    	return years;
	    }
	    
	    /**
	     * Generates a list with all days, from 1 to 31.
	     * 
	     * @return	An ArrayList containing numbers from 1 to 31,
	     * 			representing days of a month.
	     */
	    private ArrayList<String> generateDayList() {
	    	ArrayList<String> days = new ArrayList<String>();
	    	String tmp;
	    	for(int i = 0; i <= 31; i++) {
	    		tmp = Integer.toString(i);
	    		if (i < 10) {
	    			tmp = "0" + tmp;
	    		}
	    		days.add(tmp);
	    	}
	    	return days;
	    }
	    
	    /**
	     * Checks that the day given by the user has a correct format. That means a day number
	     * must not be negative, nor 0, nor exceding month's length, even considering if the given
	     * month is February and year is a leap year.
	     * 
	     * @param day		The day selected by the user.
	     * @param month		The month selected by the user.
	     * @param year		The year selected by the user.
	     * 
	     * @return			True if it's a valid day. False otherwise.
	     */
	    private boolean checkDayFormat(String day, String month, String year) {
	    	ArrayList<String> monthsWith30Days = Lists.newArrayList("April", "June", "September", "November");
	    	int numberOfDay, lastMonthDay, yearNumber = Integer.parseInt(year);
	    	try {
		    	numberOfDay = Integer.parseInt(day);
	    	} catch(NumberFormatException e) {
	    		return false;
	    	}
	    	if (numberOfDay <= 0) {
	    		return false;
	    	} else if (month.equals("February")) {
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
	    	return (numberOfDay <= lastMonthDay);
	    }
	    
	    private void executeCommand(String metric, FaceToPaint face, ColorScale color, String url, String name) {
	    	
	    	// manage invalid face selections
	    	final UILabel infoField = find("infoField", UILabel.class);
	    	if (face == null || color == null) {
        		infoField.setText("Warning: please choose a face and a color to paint!");
        		return;
        	}
        	infoField.setText("");
	    	
	    	ConsoleCommand ca = console.getCommand(new Name("paintWithGit"));	
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
