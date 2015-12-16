#import "StringsListRequest.h"

@implementation StringsListRequest

@synthesize values;

- (void) init {
  if (self = [super init]) {
    values = [[[NSMutableArray alloc] init] autorelease];
  }
  return self;
}

- (void) dealloc {
  [values release];
  [super dealloc];
}

@end
