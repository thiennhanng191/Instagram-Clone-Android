# Project 3 - *Instagram Clone*

**Instagram Clone** is a photo sharing app similar to Instagram but using Parse as its backend.

Time spent: **12** hours spent in total

## User Stories

The following **required** functionality is completed:

- [x] User can view the last 20 posts submitted to "Instagram".
- [x] The user should switch between different tabs - viewing all posts (feed view), compose (capture photos form camera) and profile tabs (posts made) using fragments and a Bottom Navigation View. (2 points) (compose is a seperate activity since I based the design based on the ios version of Instagram)
- [x] User can pull to refresh the last 20 posts submitted to "Instagram".

The following **optional** features are implemented:

- [x] User sees app icon in home screen and styled bottom navigation view
- [x] Style the feed to look like the real Instagram feed.
- [x] User can load more posts once he or she reaches the bottom of the feed using infinite scrolling.
- [x] Show the username and creation time for each post.
- [x] User can tap a post to view post details, including timestamp and caption.
- [x] User Profiles
  - [x] Display the profile photo with each post
  - [x] Tapping on a post's username or profile photo goes to that user's profile page and shows a grid view of the user's posts 
- [x] User can like a post and see number of likes for each post in the post details screen.

The following **additional** features are implemented:
- [x] Written in Kotlin
- [x] Integrate GridLayoutManager in RecyclerView for User Profile fragment


## Video Walkthrough

### Infinite Scrolling, Pull to Request, Compose and Add New Post, Implement Fragments
<img src='walkthough-asmt3pt2-1.gif' title='Login and Add Post Walkthrough' width='Infinite Scrolling, Pull to Request, Compose and Add New Post, Implement Fragments' alt='Infinite Scrolling, Pull to Request, Compose and Add New Post, Implement Fragments' />

### Tap Post to go to Post Details, Like Post, Tap on Username and Profile Photo to fo to User Profile
<img src='walkthough-asmt3pt2-2.gif' title='Tap Post to go to Post Details, Like Post, Tap on Username and Profile Photo to fo to User Profile' width='' alt='Tap Post to go to Post Details, Like Post, Tap on Username and Profile Photo to fo to User Profile' />

### Current User Profile, Display Logged In User's posts in Grid, Log Out Functionality
<img src='walkthough-asmt3pt2-3.gif' title='Walkthrough part 3' width='' alt='Walkthrough part 3' />


GIF created with [GIPHY Capture](https://giphy.com/apps/giphycapture).

## Open-source libraries used

- [Android Async HTTP](https://github.com/codepath/CPAsyncHttpClient) - Simple asynchronous HTTP requests with JSON parsing
- [Glide](https://github.com/bumptech/glide) - Image loading and caching library for Android

## License

    Copyright [2021] [MIT]

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
