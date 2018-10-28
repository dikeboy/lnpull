how  to  use

        pullLinearLayout.listView?.adapter= MAdapter()
        pullLinearLayout.setOnPullClickListener(object: PullListLayout.PullClickListener{
            override fun onRefrensh() {
                 var handler =Handler()
                handler.postDelayed({
                    pullLinearLayout.listView?.onRefreshComplete()
                },3000)

            }

            override fun onRetry() {
            }

            override fun onMoreClick(): Boolean {
                return false
            }

            override fun onRefrenshPause() {
            }
        })
        pullLinearLayout.finishLoading()
  
