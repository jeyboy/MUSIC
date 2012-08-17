# Copyright 1999-2004 Gentoo Foundation
# Distributed under the terms of the GNU General Public License v2
# $Header$

DESCRIPTION="Java-based Audio files tag editor/file renamer using freedb"
HOMEPAGE="http://entagged.sourceforge.net/"
SRC_URI="mirror://sourceforge/${PN}/${PN}-tageditor-${PV}.tar.gz"

LICENSE="GPL-2"
SLOT="0"
KEYWORDS="~x86 ~ppc"
IUSE=""

DEPEND=">=virtual/jdk-1.4
		dev-java/ant-core"
RDEPEND=">=virtual/jre-1.4"

S=${WORKDIR}/${PN}

PROGRAM_DIR=/usr/share/${PF}
PROGRAM_EXE=/usr/bin/${PN}

src_unpack() {
	local ENTAGGED_FILESDIR="${S}/entagged-build-resources"
	
	unpack ${A}
	
	#Copy provided launch script
	mv ${ENTAGGED_FILESDIR}/misc/${PN}.sh ${S}/${PN} \
		|| die "Cannot copy launch script ${ENTAGGED_FILESDIR}/misc/${PN}.sh to ${S}/${PN}"
	
	#Copy provided .desktop file
	mv ${ENTAGGED_FILESDIR}/misc/${PN}.desktop ${S}/ \
		|| die "Cannot copy ${ENTAGGED_FILESDIR}/misc/${PN}.desktop to ${S}"
	
	#Copy provided pixmap
	mv ${ENTAGGED_FILESDIR}/image/${PN}-32x32.png ${S}/${PN}.png \
		|| die "Cannot copy ${ENTAGGED_FILESDIR}/image/${PN}-32x32.png to ${S}/${PN}.png"
	
	# Set runtime settings in the startup script
	sed -i "s:##PROGRAM_DIR##:${PROGRAM_DIR}:" ${S}/${PN} \
		|| die "Cannot setup launch script !"
	sed -i "s:##PROGRAM_VERSION##:${PV}:" ${S}/${PN} \
		|| die "Cannot setup launch script !"
}

src_compile() {
	ant  \
		-buildfile build.xml build \
		|| die "ant build failed"
}

src_install() {
	dodir ${PROGRAM_DIR}
	insinto ${PROGRAM_DIR}
	
	dobin ${PN}     || die "dobin entagged failed"
	doins lib/*.jar || die "doins lib/*.jar failed"
	doins *.jar     || die "doins tageditor jar failed"
	dodoc lib/*.txt entagged-doc/*.txt
	dohtml -r entagged-doc/
	
	insinto /usr/share/pixmaps
	doins ${PN}.png \
		|| die "Cannot install pixmap !"
		
	insinto /usr/share/applications
	doins ${PN}.desktop \
		|| die "Cannot install .desktop file !"
}
