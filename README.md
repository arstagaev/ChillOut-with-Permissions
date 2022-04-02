# ChillOut with Permissions in Android!

<table style= padding:10px">
  <tr>
    <td>  <img src="./Docs/img1"  alt="1" width = 400px height = 100px ></td>
  </tr>
</table>

### For What?
---

This lightweight library needed for fast prototyping of mobile apps (where very important not only beautiful underhood of project, but and visual statement of accepting permissions).
All permission in this lib calling in one place - e.x. first screen (is useful for first stages of developing)
### How to Use?
---
Simple:
1. Initialize array with needed permissions and comments for their
2. Set activity which we must going after accepting of all permissions
3. Accept all permissions in your app
..
4. PROFIT!
```kotlin
var preparedPermissionsx = arrayListOf<NeededPermission>()
// add needed permissions:
preparedPermissionsx.add(NeededPermission(Manifest.permission.ACCESS_FINE_LOCATION,"permission to define current location)))",false))
// launch activity with permssions:
LauncherPermissions.start(preparedPermissionsx,MainActivity::class.java,this)
```