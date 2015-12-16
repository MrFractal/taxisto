#import "CardsInfoResponse.h"

@implementation CardsInfoResponse

@synthesize cardInfo;

- (void) init {
  if (self = [super init]) {
    cardInfo = [[[NSMutableArray alloc] init] autorelease];
  }
  return self;
}

- (void) dealloc {
  [cardInfo release];
  [super dealloc];
}

@end
