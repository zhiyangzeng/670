

MAX_PAGES=2000
MAX_DEPTH=150
max_output=5001
city=''
state=''


from bs4 import BeautifulSoup, Tag
from time import sleep
#import time
import re
import sys
TAG_RE = re.compile(r'<[^>]+>')
tab_re=re.compile(r'[ \t]')

'''
>>>>>>> expr
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
            
def parse_to_substr (needle, endneedle, haystack):
    start_index=haystack.find(needle)+len(needle)
    end_index=haystack.find(endneedle,start_index+1)
    return haystack[start_index:end_index]

def process_html(review_num,outlinks, reviewlist,content,proceed):
    global max_output  #100k
    global city
    global state
    global term
    parsed_html=BeautifulSoup(content,"html.parser")
    restaurant=parsed_html.find('meta', property='og:title')
    filepath="../reviews/"
    #restaurant=parsed_html.find("h1", itemprop='name')
    if restaurant:
        res_name=restaurant.get('content')
        res_name=res_name.encode('ascii', 'ignore')
        #res_name=restaurant.encode()
        
    #price=parsed_html.find('meta', itemprop="priceRange")
    price=parsed_html.find("span", { "class" : "business-attribute price-range" })
    #print price
    
        
    for link in parsed_html.find_all('a'): #finds all the urls and push into queue
        url= link.get('href') #parse relative here and stores
        if url != None :
            if '/biz/' in url:
                if url.find('/',0,1) != -1:
                    url="http://www.yelp.com"+url
                if url.find('//www.',0,len(url)) != -1 and url.find('.com/',0,len(url)) !=-1 :    
                    index=url.find('?hrid')
                    if index!=-1:
                        url=url[:index+1]
                    outlinks.append(url)
                #print url
                
    for nextBizPage in parsed_html.find_all('link', rel="next"): #parse nextpg here and stores
        #print nextBizPage
        url= nextBizPage.get('href')
        index=url.find('?hrid')
        if index!=-1:
            url=url[:index+1]
        outlinks.append(url) 
    
    for reviewsection in parsed_html.find_all('div', itemprop="review"):
        reviewID=reviewsection.find("div", { "class" : "rateReview voting-feedback" })
        reviewID_str=parse_to_substr('data-review-id="','"',str(reviewID))
        # use regex to remove tags
        if reviewID_str not in reviewlist:  #avoid duplication
            reviewlist.add(reviewID_str)
            review = reviewsection.find('p', itemprop='description')#finds reviews
            reviewStr = remove_tags(str(review))
        
            date=reviewsection.find('meta', itemprop='datePublished')
            rating=reviewsection.find('meta',itemprop='ratingValue')
        
            date_str=parse_to_substr('"datePublished">\n','\n',str(date))
            rating_str=parse_to_substr('<meta content="','"',str(rating))
            price_str=parse_to_substr('<span class="business-attribute price-range">','<',str(price))
            #resname_str=parse_to_substr('<h1 class="biz-page-title embossed-text-white" itemprop="name">\n','\n',res_name)
            date_str=remove_tabs(date_str)
        
                #print review
                #print type(review)
            if review_num<max_output:
                #print "printing review number "+str(review_num)
           
                review_str="Review ID: "
                review_str+=(reviewID_str)
                review_str+="\nPublished date: "
                review_str+=(date_str)
                review_str+="\nStars given: "
                review_str+=(rating_str)
                review_str+="\nRestaurant name: "
                review_str+=res_name
                review_str+="\nPrice range: "
                review_str+=price_str
                review_str+="\nReview text: "
                review_str+=reviewStr
                print "printing review number "+str(review_num)
                filename=filepath+term+'-'+city+'-'+state+'#'+str(review_num)+'.txt'
                #f = open("../reviews/review-%s-%s#%i.txt", %city,%state,%review_num,'w') -does not work
                f=open(filename,'w')
                f.write(review_str)
                f.close()
                review_num+=1
            else:
                proceed=False
                return proceed, outlinks, reviewlist
        
        #else:
            #print "duplicate review found!"
            
    return proceed, outlinks, reviewlist

# http://stackoverflow.com/questions/9662346/python-code-to-remove-html-tags-from-a-string
def remove_tags(text):
    return TAG_RE.sub('', text)  
    
def remove_tabs(text):
    return tab_re.sub('',text)

def crawl_web(seed,maxpage,maxdepth): # returns index, graph of inlinks
    review_num=1
    tocrawl = [seed]
    crawled = set([])
    reviewIDList = set([])
    #graph = {}  # <url>: [list of pages it links to]
    #index = {}  # keyword: [url]
    nextdepth=[]
    depth=0
    proceed=True
   
    while tocrawl and depth<=maxdepth and proceed:
        
        if len(crawled)>=maxpage: #limit number of pages
            break
            
        page = tocrawl.pop()
        if page not in crawled:
            outlinks= []
            content = get_page(page)
            
            proceed,outlinks,reviewIDList=process_html(review_num,outlinks,reviewIDList,content,proceed)    

            union(nextdepth, outlinks)
            crawled.add(page)
            #print "page crawled: "+page
        if not tocrawl:
            tocrawl,nextdepth=nextdepth,[]
            depth+=1

def main():
    global city
    global state
    global term
    
    #asian, mexican, burger, steak
    '''
    if (len(sys.argv)<4):
        print 'usage: python search.py search_term city state'
        return
    '''
    search=['asian','mexican','burger','steak']
    city_arr=['Houston','San+Francisco']
    state_arr=['TX','CA']
    base_url='http://www.yelp.com'
    #search = sys.argv[1]
    #city=sys.argv[2]
    #state=sys.argv[3]
    #location='Houston, TX'
    #term=search.replace(' ', '+')
    for y in range (0,2):
        city=city_arr[y]
        state=state_arr[y]
        for x in range (0,4):
            term=search[x]
            location=city+'%2C+'+state
            #place=location.replace(',','%2C').replace(' ','+')
            seed_query=base_url+'/search?find_desc='+term+'&find_loc='+location+'&ns=1'
            #seed_query='http://www.yelp.com/biz/houston-panini-and-provisions-houston'
            print seed_query
            crawl_web(seed_query,MAX_PAGES,MAX_DEPTH)
            sleep(60) #polite to server, avoid ban
        sleep(20)

if __name__ == "__main__":
    main()




