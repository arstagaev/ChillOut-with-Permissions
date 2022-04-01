# ChillOut with Permissions Lib.


###For What?
---

asdasd
### How to Use?
---
Simple:

```kotlin
var preparedPermissionsx = arrayListOf<NeededPermission>()
preparedPermissionsx.add(NeededPermission(Manifest.permission.ACCESS_FINE_LOCATION,"permission to define current location)))",false))

LauncherPermissions.start(preparedPermissionsx,MainActivity::class.java,this)
```