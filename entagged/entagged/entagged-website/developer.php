<?
	$Title = "Developer";
	$ID = '$Id: home.php,v 1.7 2004/10/30 23:06:05 kikidonk Exp $';
	
	require ("header.php");
?>

<div class="lastpost">
<h2>Entagged as File Tagging API</h2>
<p>Beside the Tag Editor, we also maintain a library to access/modify tag data in all the supported formats, which the tag editor uses as back-end.</p>
<p>The library is available for JAVA under the LGPL license, and for C-Sharp (mono/.net) under the MIT X11 License</p>

<h2>JAVA</h2>
<h3>Javadoc API</h3><a name="javadoc" ></a>
<p>You can browse our <a href="./javadoc/index.html">Javadoc online</a>, it contains the complete methods reference as well as usage samples.
	The Main entry point in the library is the class <code>AudioFileIO</code>.</p>

<h3>Source Code</h3><a name="javasource" ></a>
<h4>Obtaining entagged sources via CVS</h4>
<p>The source code of Entagged can be browsed online via the <a href="http://cvs.sourceforge.net/viewcvs.py/entagged/entagged/">web CVS interface</a>.</p>
<p>You can also retreive a local copy on your hard-disk by running<br/>
	<code>cvs -z3 -d:pserver:anonymous@cvs.sourceforge.net:/cvsroot/entagged co -P entagged</code></p>
	
<h4>Downloading source tarballs</h4>
<p>The source code of the latest release of the libs can be <a href="http://sourceforge.net/project/showfiles.php?group_id=88759&package_id=124741">downloaded here</a>, but it doesn't contain all the latest changes (see CVS for that).</p>

<h4>Building entagged from source</h4>
<p>To build entagged libraries (and also the tag editor) you need "ant" the make-like application for java programs.</p>
<p><code>$ ant compile</code> will compile the code in place, <code>$ ant package</code> will produce a jar file with the fresh compiled code</p>

<h3>Pre-built JAR packages</h3>
<p>We also provide a <a href="http://sourceforge.net/project/showfiles.php?group_id=88759&package_id=124741">single JAR file</a> to include with your project if that's easier for you</p>

<h2>C-Sharp (Mono, .Net)</h2>
<h3>API Documentation</h3><a name="monodoc" ></a>
<p>Not available for the moment</p>

<h3>Source Code</h3><a name="monosource" ></a>
<p>Entagged-sharp is hosted on the <a href="http://www.go-mono.com">Mono Project</a> <a href="http://svn.myrealbox.com/viewcvs/trunk/entagged-sharp/">SVN repository</a>, and co-developped by <a href="http://aaronbock.net/journal/index.php/entryId=5:17">Aaron Bockover</a></p>
<h4>Obtaining entagged sources via SVN</h4>
<p>The source code of Entagged-sharp can be browsed online via the <a href="http://svn.myrealbox.com/viewcvs/trunk/entagged-sharp/">web SVN interface</a>.</p>
<p>You can also retreive a local copy on your hard-disk by running<br/>
	<code>svn co svn://svn.myrealbox.com/source/trunk/entagged-sharp</code></p>
	
<h4>Downloading source tarballs</h4>
<p>There are no source tarballs released yet for this project.</p>

<h4>Building entagged-sharp from source</h4>
<p>Entagged-sharp uses the autotools, so the standard procedure applies: <code>./configure;make;make install</code></p>

<h2>Contributing</h2>
<p>We love contributions, translations, new file formats, new UI features, etc. If you have any idea on improving entagged, please <a href="./contact.php#mailing">contact us</a></p>

</div>

<?
	require ("footer.php");
?>
