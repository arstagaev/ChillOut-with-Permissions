# ChillOut with Permissions in Android!

<table style= padding:10px">
  <tr>
    <td>  <img src="./Docs/img1.png"  alt="1" width = 400px height = 100px ></td>
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
2. Initialize core of lib
3. Accept all permissions in your app
..
4. PROFIT!

### What need for work?
1. Jetpack Compose

```kotlin

 /**
 * Should put some needed permissions in preparedPermissions array
 */

preparedPermissions.add( NeededPermission(Manifest.permission.ACCESS_FINE_LOCATION,"permission to define current location"))
preparedPermissions.add( NeededPermission(Manifest.permission.ACCESS_BACKGROUND_LOCATION,"permission to define current location, butworked in background"))
preparedPermissions.add(NeededPermission(Manifest.permission.CAMERA,"permission for camera"))

var core = CoreOfChillOut(this) // initialize core module

// 
setContent {
    ChillOutPermisionsTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            CoreOfChillOut.HorizontalPagerPermissions(this@MainActivity)
        }
    }
}

```