#import "CardsInfoRequest.h"

@implementation CardsInfoRequest

@synthesize clientId;
@synthesize cardInfo;

- (void) dealloc {
  [cardInfo release];
  [super dealloc];
}

@end
