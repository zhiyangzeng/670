

MAX_PAGES=20
MAX_DEPTH=6

from bs4 import BeautifulSoup
import time

def time_execution(code):
    start=time.clock()
    result=eval(code)
    run_time=time.clock-start
    return result, run_time
            
#opens web containing all info
def get_page(url):
    try:
        import urllib
        return urllib.urlopen(url).read()
    except:
        return ''

def union(a, b):
    for e in b:
        if e not in a:
            a.append(e)



def crawl_web(seed,maxpage,maxdepth): # returns index, graph of inlinks
    tocrawl = [seed]
    crawled = []
    graph = {}  # <url>: [list of pages it links to]
    #index = {}  # keyword: [url]
    nextdepth=[]
    depth=0
    
    while tocrawl and depth<=maxdepth: 
        
        if len(crawled)>=maxpage: #limit number of pages
            break
            
        page = tocrawl.pop()
        if page not in crawled:
            outlinks= []
            content = get_page(page)
            parsed_html=BeautifulSoup(content)
            
            for link in parsed_html.find_all('a'):
                url= link.get('href') #parse here
                if url != None :
                    if url.find('/',0,1) != -1:
                	    url="http://yelp.com"+url
                    outlinks.append(url)
                    print url
            
            print parsed_html.find_all('review-content')
                 
            
            #add_page_to_index(index, page, content)
            #outlinks = get_all_links(content)
            graph[page] = outlinks
            union(nextdepth, outlinks)
            crawled.append(page)
            print "page crawled: "+page
        if not tocrawl:
            tocrawl,nextdepth=nextdepth,[]
            depth+=1
            
    #return index, graph


 #returns a string with 
def main():
    base_url='http://www.yelp.com'
    search = 'noodles'
    location='Houston, TX'
    term=search.replace(' ', '+')
    place=location.replace(',','%2C').replace(' ','+')
    #seed_query=base_url+'/search?find_desc='+term+'&find_loc='+place+'&ns=1#start=0'
    seed_query='http://www.yelp.com/biz/korean-noodle-house-houston?osq=noodles'
    #index, graph = crawl_web(seed_query,MAX_PAGES,MAX_DEPTH)
    crawl_web(seed_query,MAX_PAGES,MAX_DEPTH)
    #ranks = compute_ranks(graph)
    #print search(index, ranks, 'Japanese')

if __name__ == "__main__":
    main()




