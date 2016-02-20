

MAX_PAGES=10
MAX_DEPTH=5
max_output=10
review_num=1


from bs4 import BeautifulSoup
import time
'''
def time_execution(code):
    start=time.clock()
    result=eval(code)
    run_time=time.clock-start
    return result, run_time
'''    
            
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

def process_html(outlinks, content):
    global max_output  #100k
    global review_num
    parsed_html=BeautifulSoup(content,"html.parser")
    
    for link in parsed_html.find_all('a'): #finds all the urls and push into queue
        url= link.get('href') #parse relative here and stores
        if url != None :
            if '/biz/' in url:
                if url.find('/',0,1) != -1:
                    url="http://www.yelp.com"+url
                if url.find('//www.',0,len(url)) != -1 and url.find('.com/',0,len(url)) !=-1 :                                            
                    outlinks.append(url)
                #print url
    for nextBizPage in parsed_html.find_all('link', rel="next"): #parse nextpg here and stores
        print nextBizPage
        url= nextBizPage.get('href')
        outlinks.append(url) 
        
    for reviewsection in parsed_html.find_all('div', itemprop="review"):
        review = reviewsection.find('p', itemprop='description')#finds reviews
        date=reviewsection.find('meta', itemprop='datePublished')
        rating=reviewsection.find('meta',itemprop='ratingValue')
        reviewID=reviewsection.find("div", { "class" : "rateReview voting-feedback" })
        print rating
        print date
        print reviewID
            #print review
            #print type(review)
        if review_num<max_output:
            #print "printing review number "+str(review_num)
            f = open("reviews/review#%i.txt" %review_num,'w')
            review_str=str(review)
            review_str+="\n"
            review_str+=(str(reviewID))
            review_str+="\n"
            review_str+=(str(date))
            review_str+="\n"
            review_str+=(str(rating))
            #review_str.append
            f.write(review_str)
            f.close()
            review_num+=1
    return outlinks
                
'''    
    for review in parsed_html.find_all('p', itemprop='description'):#finds reviews
        #print review
        #print type(review)
        if review_num<max_output:

            #print "printing review number "+str(review_num)
            f = open("reviews/review#%i.txt" %review_num,'w')
            f.write(str(review))
            f.close()
            review_num+=1
'''
     

def crawl_web(seed,maxpage,maxdepth): # returns index, graph of inlinks

    tocrawl = [seed]
    crawled = []
    #graph = {}  # <url>: [list of pages it links to]
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
            
            outlinks=process_html(outlinks,content)    
            
            
            #add_page_to_index(index, page, content)
            #outlinks = get_all_links(content)
            #graph[page] = outlinks
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
    seed_query=base_url+'/search?find_desc='+term+'&find_loc='+place+'&ns=1#start=0'
    print seed_query
    #index, graph = crawl_web(seed_query,MAX_PAGES,MAX_DEPTH)
    crawl_web(seed_query,MAX_PAGES,MAX_DEPTH)
    #ranks = compute_ranks(graph)
    #print search(index, ranks, 'Japanese')

if __name__ == "__main__":
    main()




