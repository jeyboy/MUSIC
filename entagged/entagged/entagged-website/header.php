<?
	//Misc configs--------------------------------------------------------------
	/*
	INITIALIZED VARIABLES:
		IMGEXT: extension of the images ("png" or "gif")
		PAGE: name of the browsed page
	*/
	
	//Search for User Agent String and test whether the transparent png is supported stores in IMGEXT
	$IMGEXT =  "gif";
	if( ereg("^.*Opera ([6-9]|([1-9][0-9]\.)).*$", $_SERVER['HTTP_USER_AGENT']) ||   //Opera >= 6.00
		ereg("^.*Gecko\/2[0-9]{2}[1-9].*$", $_SERVER['HTTP_USER_AGENT'])  )  //Gecko > 2001
		$IMGEXT = "png";
	//-------------------------------------------------------------------------------
	
	
	//Initializes the PAGE variable used to navigate through the site
	if(isset($_GET['page']))
		$PAGE = $_GET['page'];
	else
		$PAGE = "home";
	//----------------------------------------------------------------
	
	//-------MAX RSS FEED RETRY---------
	$MAX_RETRY = 2;
	
	//--ERRORS REPORT
	//error_reporting(E_ERROR | E_WARNING | E_PARSE | E_NOTICE);
	error_reporting(E_ERROR | E_WARNING | E_PARSE);
	//--------------------------------------------------------------------------
	
	//Rss feeds
	require("magpierss/rss_fetch.inc");
	$ID = '$Id: index.php,v 1.12 2005/01/17 18:24:28 kikidonk Exp $';
	
	echo '<?xml version="1.0" encoding="UTF-8"?>';
?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
	<head>
		<title>Entagged | <? echo $Title; ?></title>
		<meta name="author" content="Raphael Slinckx (KiKiDonK)" />
		<meta name="description" content="Home Page of the Entagged Sourceforge project. Entagged is an audio files (mp3, ogg, wma, etc) tagger." />
		<meta name="keywords" content="entagged,mp3,ogg,open,source,open source,sourceforge,tag,tagger,audio,manage,organize,collection,library,database,java,csharp" />
		<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
		
		<link href="http://raphael.slinckx.net/style.css" rel="stylesheet" type="text/css" />
		<link rel="icon" href="http://raphael.slinckx.net/favicon.ico" type="image/ico" />
		
		<style type="text/css">
			/*Defines the background image wich is UA dependent*/
			.post {
				background-image: url(images/bglogo.<?echo $IMGEXT;?>);
				background-position: center;
				background-repeat: no-repeat;
			}
			#header h1 {
				display: none;
			}
			#header img.altlogo {
				margin-bottom: 10px;
				padding-top: 70px;
			}
			
		</style>
		<script src="http://www.google-analytics.com/urchin.js" type="text/javascript"></script>
		<script type="text/javascript">
		_uacct = "UA-94765-2";
		urchinTracker();
		</script>
	</head>

	<body>
		<img id="logoheader" src="images/logo.<?echo $IMGEXT;?>" alt="Header Logo"/>
				
		<div id="page">
			<div id="header">
				<h1><a href="index.php">Entagged</a></h1>
				<img class="altlogo" src="images/entagged.<?echo $IMGEXT;?>" alt="Entagged Logo"/>
				<p class="subtitle"><? echo $Title; ?></p>
			</div>
			<hr />
			
			<div id="content" class="widecolumn">
				<div id="sidebar">
					<ul>
						<li>
							<h2><a href="./index.php">Home</a></h2>
							<ul>
								<li><a href="./index.php#news">News</a></li>
								<li><a href="./index.php#download">Download</a></li>
								<li><a href="./index.php#reports">Bugs and feature requests</a></li>
								<li><a href="http://www.sf.net/projects/entagged">Sourceforge Page</a></li>
							</ul>
						</li>
						<li>
							<h2><a href="./about.php">About</a></h2>
							<ul>
								<li><a href="./about.php#tageditor">Tag Editor</a></li>
								<li><a href="./about.php#screenshots">Screenshots</a></li>
								<li><a href="./changelog.txt">Changelog</a></li>
								<li><a href="./about.php#credits">Credits</a></li>
							</ul>
						</li>
						<li>
							<h2><a href="./developer.php">Developer</a></h2>
							<ul>
								<li><a href="./developer.php#javadoc">Java doc</a></li>
								<li><a href="./developer.php#source">Java Source Code</a></li>
								<li><a href="./developer.php#monodoc">csharp doc</a></li>
								<li><a href="./developer.php#monosource">csharp Source Code</a></li>
							</ul>
						</li>
						<li>
							<h2><a href="./contact.php">Contact</a></h2>
							<ul>
								<li><a href="http://entagged.sourceforge.net/forum/">Forum</a></li>
								<li><a href="./contact.php#mailing">Mailing Lists</a></li>
								<li><a href="./contact.php#crew">Crew</a></li>
							</ul>
						</li>
						<li>
							<a href="http://sourceforge.net/donate/index.php?group_id=88759"><img src="http://images.sourceforge.net/images/project-support.jpg" width="88" height="32" border="0" alt="Support This Project" /></a>
						</li>
					</ul>
				</div>
				
