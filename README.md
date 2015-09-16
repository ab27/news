Comments
========
when a comment is clicked MainActivity gets notified because it
implements the ArticleCallbacks interface... which has a method
onArticleCommentsClicked

to call the interface implemented by MainActivity HNfragment and
RedditFragment must setup the callback onAttach 
# news
