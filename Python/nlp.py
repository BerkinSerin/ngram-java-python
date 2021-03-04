import re
import time

start = time.time()
# dictionaries for n-grams
unidict = {}
bidict = {}
tridict = {}

# regexes to match ?.. !.. and other repeating chars such as ...    
regexes = [
    r'[?!]?[.]+',
    r'[«?{(].{1}[»?})]',
    r'[^a-zA-Z0-9güsâöiçIÂGÜSÖÇ]'
]

# combine regexes into one ultimate regex
combined = "(" + ")|(".join(regexes) + ")"

# function to get the final version of the given string 
def getFinalString (path):
    # read the file and join the paragraphs into one string
    corpus = open(path, encoding='ISO-8859-9').read()
    corpus = corpus.replace('\r', ' ')
    corpus = corpus.replace('\n', ' ').strip()
    
    
    # seperate punctuations and special characters (!.. ?.. (?) ......)
    
    corpus = re.sub(combined,' \g<0> ', corpus) #this will match the first regex it matches on the string 
    corpus = re.sub(' +', ' ', corpus)
    return corpus

# paths for the documents and a list to keep them all
paths = []
path1 = "C:/Users/berki/Desktop/nlp_1/BILIM IS BASINDA.txt"
path2 = "C:/Users/berki/Desktop/nlp_1/BOZKIRDA.txt"
path3 = "C:/Users/berki/Desktop/nlp_1/DEGISIM.txt"
path4 = "C:/Users/berki/Desktop/nlp_1/DENEMELER.txt"
path5 = "C:/Users/berki/Desktop/nlp_1/UNUTULMUS DIYARLAR.txt"

paths.append(path1)
paths.append(path2)
paths.append(path3)
paths.append(path4)
paths.append(path5)

# corpus to generate n-grams from
corpus = ''

# create ultimate final corpus (append each document to the corpus)
for path in paths:
    corpus = corpus + ' ' + getFinalString(path)
    corpus = corpus.strip()
    
# function for ngrams
def get_ngrams(wordlist,n):
    wordlist = [x.lower() for x in wordlist]
    wordlist = [x.replace(' +', ' ') for x in wordlist]
    ngrams = {}
    for i in range(len(wordlist)-(n-1)):
        if ' '.join(wordlist[i:i+n]) in ngrams:
            ngrams[' '.join(wordlist[i:i+n])] += 1
        else:
            ngrams[' '.join(wordlist[i:i+n])] = 1
            
    return sorted(ngrams.items(), key=lambda x: x[1], reverse=True)[:100] # sort them descending and return top 100

unidict = get_ngrams(corpus.split(' '), 1)
bidict = get_ngrams(corpus.split(' '), 2)
tridict = get_ngrams(corpus.split(' '), 3)

print('Top 100 1-grams for the corpus: \n')
print(unidict)
print('\n')
print('Top 100 2-grams for the corpus: \n')
print(bidict)
print('\n')
print('Top 100 3-grams for the corpus: \n')
print(tridict)
print('\n')
end = time.time()
print('Runtime in milliseconds: {0}'.format(end - start))