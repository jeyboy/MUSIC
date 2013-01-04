<?
	$Title = "About";
	$ID = '$Id: home.php,v 1.7 2004/10/30 23:06:05 kikidonk Exp $';
	
	require ("header.php");
?>

<div class="lastpost">
	<h2>Entagged, The Tag Editor</h2><a name="tageditor"></a>
	<p><em>(This text contains what entagged want to achieve, the actual state of things may differ.)</em></p>

	<h3>Entagged is an Open-source audiofile tagger.</h3>
	<p>You are wondering what the hell is a tag then ?</p>
	<p>A tag is a piece of information stored inside the audio file that contains the title, artist, track number, and other data relative to that file.
	Every audio format has its own way of storing this data, the MP3 uses ID3 tags, OGG uses it's own tag format and so on (WMA, APE, MPC,...).
	This data is used by many audio players (winamp, xmms, ..) , to display artist name, album title etc.</p>

	<h3>So what can Entagged do ?</h3>
	<p>Entagged provides a convenient way of changing the value of these fields, either manually, or automatically via the <a href="http://www.freedb.org">freedb</a> database.
	The <a href="http://www.freedb.org">freedb</a> database is a server to wich you can connect, and ask about a precise CD (album, or compilation), if this CD is found in the database, it returns all the data needed to correctly tag your audio files.
	Originally it used only with CD, but it can also be used with audio files as soon as it only need the length (in seconds) of the files.</p>
	<p>Entagged can also do a lot of organization work. You can rename your files from their tag in any way you like, including complex directory structures. You can also tag the files from their filename, this avoid copying all the information by hand.</p>
	<p>Best of all, Entagged hides all the different format complexity, with one unique interface, you can work with mixed file formats eg. you can use freedb with a mixed album of WMA, MP3 and OGG files without worrying!</p>

	<h3>How can I try it ?</h3>
	<p>Simply go to the <a href="./index.php#download">download area</a> and read carefully the instructions.</p>


	
	<h2>Entagged, The File tagging library</h2>
	<p>We have a <a href="./developer.php">whole section</a> dedicated to the use of entagged file tagging library with examples and APIs</p>

	<h2>Screenshots</h2><a name="screenshots" ></a>
	<p>These are screenshots of current and less current versions !</p>
	<p>Click on a screenshot to get a full size picture...</p>

	<div class="screenshot">
		<a href="./images/entagged-tageditor-general.png">
			<img src="./images/entagged-tageditor-general-small.png" alt="screenshot"/>
			<p>The general view</p>
		</a>
	</div>
	
	<div class="screenshot">
		<a href="./images/entagged-tageditor-freedb.png">
			<img src="./images/entagged-tageditor-freedb-small.png" alt="screenshot"/>
			<p>The freedb feature</p>
		</a>
	</div>
	<div class="screenshot">
		<a href="./images/entagged-tageditor-tageditortab.png">
			<img src="./images/entagged-tageditor-tageditortab-small.png" alt="screenshot"/>
			<p>Tag Editor</p>
		</a>
	</div>
	<div class="screenshot">
		<a href="./images/entagged-tageditor-renamefromtag.png">
			<img src="./images/entagged-tageditor-renamefromtag-small.png" alt="screenshot"/>
			<p>The rename from tag feature</p>
		</a>
	</div>
	<div class="screenshot">
		<a href="./images/entagged-tageditor-tagfromfilename.png">
			<img src="./images/entagged-tageditor-tagfromfilename-small.png" alt="screenshot"/>
			<p>The tag from filename feature</p>
		</a>
	</div>

	<h2>Changelog</h2><a name="changelog" ></a>
	<p>You can consult the latest <a href="./changelog.txt">Changelog</a> online, and you can also find it in the package you downloaded</p>
	
	<h2>Credits</h2><a name="credits" ></a>
	<p>People I wish to thank</p>
	<ul>
		<li>Every random contributor,translators,coders,..</li>
		<li><a href="http://4rion.free.fr/">Arion</a> for the design of the Entagged logo</li>
		<li><a href="http://www.sourceforge.net/">Sourceforge</a> for the hosting of the project</li>
	</ul>
</div>

<?
	require ("footer.php");
?>
