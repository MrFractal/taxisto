#import "CardsResponse.h"

@implementation CardsResponse

@synthesize cards;

- (void) init {
  if (self = [super init]) {
    cards = [[[NSMutableArray alloc] init] autorelease];
  }
  return self;
}

- (void) dealloc {
  [cards release];
  [super dealloc];
}

@end
