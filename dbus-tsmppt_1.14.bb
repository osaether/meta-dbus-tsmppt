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

pkg_postinst_${PN} () {
	if test "x$D" = "x"; then
		pushd /opt/victronenergy/gui/qml
		cp /opt/victronenergy/dbus-tsmppt/PageSettingsTsmppt.qml .
		cp PageSettings.qml PageSettings.qml.save
		cp PageSolarCharger.qml PageSolarCharger.qml.save
		svc -d /service/gui
		patch -p1 PageSettings.qml /opt/victronenergy/dbus-tsmppt/PageSettings.diff
		patch -p1 PageSolarCharger.qml /opt/victronenergy/dbus-tsmppt/PageSolarCharger.diff
		svc -u /service/gui
		popd
	fi
}

pkg_postrm_${PN} () {
	if test "x$D" = "x"; then
		pushd /opt/victronenergy/gui/qml
		svc -d /service/gui
		if [ -e "PageSettings.qml.save" ]; then
			mv -f PageSettings.qml.save PageSettings.qml
		fi
		if [ -e "PageSolarCharger.qml.save" ]; then
			mv -f PageSolarCharger.qml.save PageSolarCharger.qml
		fi
		rm -f PageSettingsTsmppt.qml
		svc -u /service/gui
		popd
	fi
}
