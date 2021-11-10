# JavaDoctolib - CalendUp

## Introduction

Application project allowing patients to make appointments with doctors registered on the application.\
Management of notifications, appointment booking, calendar of upcoming events...


## Module : 

```
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
```

## Couleur de reference pour la GUI

couleur de fond :			`#83c1f5`\
couleur d'un element :		`#62aff0`\
couleur de mise en avant :  	`#59a0dc`\
couleur de contour : 		`#487aa5`\
couleur text mise en avant : `white`

## Authors

Alexis MARTIN - https://github.com/AlexisMartin44\
Ulysse VINCENTI - https://github.com/joaquimelerebel