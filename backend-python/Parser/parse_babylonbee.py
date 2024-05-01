import pandas as pd
import requests
from bs4 import BeautifulSoup

df = pd.read_parquet('satire_data.parquet', columns=['url', 'headline']).head(50)


def parse_url(url):
    try:
        response = requests.get(url)
        soup = BeautifulSoup(response.text, 'html.parser')
        p_tags = soup.select('#articleShowContent > div.flex.flex-col.bg-gray-50.rounded.shadow-md.overflow-hidden.mb'
                             '-4 > div.flex-1.bg-white.pt-0.sm\\:px-6.pb-4.flex.flex-col.justify-between > div > div p')
        content = ''.join(tag.text.strip() for tag in p_tags)
        return content.strip()
    except Exception as e:
        print(f"Error while reading URL {url}: {e}")
        return None


df['article_text'] = df['url'].apply(parse_url)

df.to_csv('../parser_result.csv', index=False)
