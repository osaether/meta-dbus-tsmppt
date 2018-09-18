LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

inherit qmakeve
inherit daemontools

DEPENDS += "libmodbus"

SRC_URI = " \
        gitsm://github.com/osaether/dbus-tsmppt.git;tag=v${PV};protocol=https \
"

S = "${WORKDIR}/git/software"

DEST_DIR = "${D}${bindir}"

DAEMONTOOLS_SERVICE_DIR = "${bindir}/service"
DAEMONTOOLS_RUN = "softlimit -d 100000000 -s 1000000 -a 100000000 ${bindir}/${PN}"

# why? dbus connection, will be fixed when switching to common code..
EXTRA_QMAKEVARS_POST += "DEFINES+=TARGET_beaglebone"

