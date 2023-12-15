# NewsFeed

*NewsFeed - simple client for NewsApi.org*

Used Dagger Hilt, Coroutines, Flow, Room, ViewModel, Paging3, Glide.
Used clean MVVM architecture.

Features: 
- List news with endless scrolling (note that the usage of this API is restricted for free users up to 100 requests per one day)
- Search news by keywords
- Open details by clicking on item. 
- Add to favorites
- Offline mode for favorites
- Dark theme supported
- Swipe to delete from favorites with undo functionality

*TODO*:


*DONE*
- fix RETRY button to reload news when connection is available
- feat: add empty state for favorites
- feat: add filter news by category

# To Get Api Key
Go to https://newsapi.org/account
Create an account
Get an API Key

Add this key in local.properties file as `NEWS_API_KEY="Your API key here"`
