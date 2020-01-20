# NFSee

**Note: Development of this repo is discontinued. It will be replaced with a new [flutter version](https://github.com/nfc.im/nfsee) that can run on both Android and iOS.**

[![NFSee on Google Play](https://raw.githubusercontent.com/steverichey/google-play-badge-svg/master/img/en_get.svg)](https://play.google.com/store/apps/details?id=im.nfc.nfsee)

NFSee is an Android application that can:

* read various type of NFC tags / cards,
* execute user-specified scripts on NFC tags.

You can download it in [Google Play](https://play.google.com/store/apps/details?id=im.nfc.nfsee).

## Build

You can use `gradle` or Android Studio to build the application.

## Supported cards

Refer to [L4.kt](https://github.com/nfcim/nfsee-legacy/blob/master/app/src/main/java/im/nfc/nfsee/models/L4.kt) for all supported card.

## Script engine

NFSee has a builtin Lua script engine that can transceive APDU with cards compliant with ISO 7816.
