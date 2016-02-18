

MAX_PAGES=20

from bs4 import BeautifulSoup
import time

def time_execution(code):
    start=time.clock()
    result=eval(code)
    run_time=time.clock-start
    return result, run_time
    
    
def search(index, ranks, keyword):
    link=''
    for key in index:
        if key==keyword: #found keyword
            urls= index[keyword]  #url list for the keyword
            max_num=0.0
            
            for url in urls:  #through list of url
                if url in ranks:
                    if ranks[url]>max_num:
                        max_num=ranks[url]
                        link=url
            return link
        
    return None
        
            
#opens web containing all info
def get_page(url):
    try:
        import urllib
        return urllib.urlopen(url).read()
    except:
        return ''


    
def split_string(source, splitlist):
    output=[]
    atsplit=True
    for char in source:
        if char in splitlist:
            atsplit=True
        else:
            if atsplit:
                output.append(char)
                atsplit=False
            else:
                output[-1]=output[-1]+char
    return output

def get_next_target(page):
    start_link = page.find('href=')
    if start_link == -1: 
        return None, 0
    start_quote = page.find('"', start_link)
    end_quote = page.find('"', start_quote + 1)
    url = page[start_quote + 1:end_quote]
    return url, end_quote

#stores url in links and calls the crawler to the next url, return links at end of page
def get_all_links(page):
    links = []
    while True:
        url, endpos = get_next_target(page)
        if url:
            links.append(url)
            page = page[endpos:]
        else:
            break
    return links


def union(a, b):
    for e in b:
        if e not in a:
            a.append(e)

def add_page_to_index(index, url, content):
    #words = content.split()
    words=split_string(content, ',!?=+ \#<>')
    
    for word in words:
        add_to_index(index, word, url)
        
def add_to_index(index, keyword, url):
    if keyword in index:
        index[keyword].append(url)
    else:
        index[keyword] = [url]
    
def lookup(index, keyword):
    if keyword in index:
        return index[keyword]
    else:
        return None

def crawl_web(seed,maxpage,maxdepth): # returns index, graph of inlinks
    tocrawl = [seed]
    crawled = []
    graph = {}  # <url>: [list of pages it links to]
    index = {}  # keyword: [url]
    nextdepth=[]
    depth=0
    
    while tocrawl and depth<=maxdepth: 
        
        if len(crawled)>=maxpage: #limit number of pages
            break
            
        page = tocrawl.pop()
        if page not in crawled:
            content = get_page(page)
            
            parsed_html=BeautifulSoup(content)
            
            for link in parsed_html.find_all('a'):
                print link.get('href')
            
            print parsed_html.find_all('review-content')
                 
            
            add_page_to_index(index, page, content)
            outlinks = get_all_links(content)
            graph[page] = outlinks
            union(nextdepth, outlinks)
            crawled.append(page)
            print "page crawled: "+page
        if not tocrawl:
            tocrawl,nextdepth=nextdepth,[]
            depth+=1
            
    return index, graph

def compute_ranks(graph):
    d = 0.8 # damping factor
    numloops = 10
    
    ranks = {}
    npages = len(graph)
    for page in graph:
        ranks[page] = 1.0 / npages
    
    for i in range(0, numloops):
        newranks = {}
        for page in graph:
            newrank = (1 - d) / npages
            for node in graph:
                if page in graph[node]:
                    newrank = newrank + d * (ranks[node] / len(graph[node]))
            newranks[page] = newrank
        ranks = newranks
    return ranks

 #returns a string with 
def main():
    base_url='http://www.yelp.com'
    search = 'noodles'
    location='Houston, TX'
    term=search.replace(' ', '+')
    place=location.replace(',','%2C').replace(' ','+')
    seed_query=base_url+'/search?find_desc='+term+'&find_loc='+place+'&ns=1#start=0'
    #print seed_query
    index, graph = crawl_web(seed_query,MAX_PAGES,5)
    ranks = compute_ranks(graph)
    print search(index, ranks, 'Japanese')

if __name__ == "__main__":
    main()




