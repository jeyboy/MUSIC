<?
	$Title = "Home";
	$ID = '$Id: index.php,v 1.13 2005/06/26 19:19:36 kikidonk Exp $';
	
	require ("header.php");
?>


<div class="lastpost">

<h2>Latest News</h2><a name="news"></a>
<?
$url = 'http://sourceforge.net/export/rss2_projnews.php?group_id=88759&rss_fulltext=1';
$MAXITEMS = 3;

$retry=0;
while($retry<$MAX_RETRY) {
	$rss = fetch_rss($url);
	if($rss) {	
		$count=0;
		foreach ($rss->items as $item ) {
			if($count >= $MAXITEMS)
				continue;
			
			$title = $item['title'];
			$description   = $item['description'];
			$pubdate = $item['pubdate'];

			echo "<h3>$title</h3><small>$pubdate</small>
		  	      <p>$description</p>";
		  	  
			$count++;
		}
		$retry = $MAX_RETRY;
	}
	$retry++;
}

if(!$rss)
	echo "<p>Unable to fetch rss feed, try refreshing the page: ".magpie_error()."</p>";
?>

<h2>Latest topics on the Forum</h2>
<ul>
<?
$url = 'http://entagged.sourceforge.net/forum/rss.php?c=5';

$retry=0;
while($retry<$MAX_RETRY) {
	$rss = fetch_rss($url);
	if($rss) {
		$maxcount = 5;
		$count=0;
		foreach ($rss->items as $item ) {
			if($count < 5) {
				$title = $item['title'];
				$link   = $item['link'];
				$description = $item['description'];
				$pubdate = isset($item['pubdate']) ? "|".$item['pubdate']."|" : "";
				$author = isset($item['author']) ? "by ".$item['author'] : "";
				echo "<li><a href=\"$link\">$title $author</a> <span style=\"font-size: 7pt;\">$pubdate</span></li>";
			}
			$count++;
		}
		$retry = $MAX_RETRY;
	}
	$retry++;
}

if(!$rss) {
	echo "<li>Unable to fetch rss feed, try refreshing the page</li>";
	echo "<li><span style=\"font-size: 7pt;\">".magpie_error()."</span></li>";
}
?>
</ul>

<h2>Download</h2><a name="download"></a>
<p>To run Entagged, you need first of all to download the Java Runtime Environment (JRE) because Entagged is written in Java. This ensures a great crossplatform compatibility at the (little: approx. 10Mb) cost of these libraries.</p>
<p>You can get the <a href="http://java.sun.com/j2se/1.4.2/download.html">latest Sun JRE</a> (Entagged needs at least JRE v1.4.1 to work properly !).</p> 
<p>Select the "Download J2SE v 1.4.xxx" column and pick under "JRE" your platform's version.</p>
<p>You can then <a href="http://sourceforge.net/project/showfiles.php?group_id=88759&package_id=92893">download Entagged</a> at sourceforge file release section, make sure you are downloading the latest version !</p>
<p>Entagged is packaged in three different ways:</p>
<ul>
	<li>"entagged-install-vx.xx.exe" is the windows installer version of this package (note that it is only a .exe wrapper to lauch the jar file, no native code here)</li>
	<li>"entagged-vx.xx.zip" is the "all platform" package, it contains mainly the executable jar file, and some images files (make sure you extract this archive using the directory structure inside it)</li>
	<li>"entagged-src-vx.xx.tar.gz" wich contains the source code and the resources needed to compile and build entagged (ant file, images, NSIS script, etc). This package is NOT needed if you only want to use Entagged (this is a developper resource)</li>
</ul>
		

<h2>Bug Reports</h2><a name="reports" ></a>
<p>Found a bug ? Please <a href="http://sourceforge.net/tracker/?func=add&amp;group_id=88759&amp;atid=587820">report it</a> on the sourceforge project page, so it will come directly in our mailboxes !</p>
<p>You will be asked to fill in some informations, please assign you report to "kikidonk" or it might pass unseen</p>

<h2>Feature Requests</h2>
<p>You can post a <a href="http://sourceforge.net/tracker/?func=add&amp;group_id=88759&amp;atid=587823">feature request</a> on the sourceforge project page, so it will come directly in our mailboxes !</p>
<p>You will be asked to fill in some informations, please assign you report to "kikidonk" or it might pass unseen</p>

<h2>Project Page</h2>
<p>For more informations, you can browse our <a href="http://www.sf.net/projects/entagged">sourceforge project site</a></p>


</div>

<?
	require ("footer.php");
?>
