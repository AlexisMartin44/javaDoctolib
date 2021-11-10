module GUI 
{
	exports gui;
	exports application;
	

	requires javafx.base;
	requires javafx.controls;
	requires javafx.fxml;
	requires javafx.graphics;
	requires javafx.web;
	requires java.sql;
	
	opens gui;
}